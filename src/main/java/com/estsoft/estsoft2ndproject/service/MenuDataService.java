package com.estsoft.estsoft2ndproject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.PostType;
import com.estsoft.estsoft2ndproject.domain.dto.post.CommentResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.repository.CategoryRepository;
import com.estsoft.estsoft2ndproject.repository.CommentRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.RegionRepository;
import com.estsoft.estsoft2ndproject.domain.Region;
import com.estsoft.estsoft2ndproject.domain.Category;

@Service
public class MenuDataService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final RegionRepository regionRepository;
	private final CategoryRepository categoryRepository;

	public MenuDataService(PostRepository postRepository, CommentRepository commentRepository,
		RegionRepository regionRepository, CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.regionRepository = regionRepository;
		this.categoryRepository = categoryRepository;
	}

	// 알림 데이터 가져오기
	@Transactional(readOnly = true)
	public List<CommentResponseDTO> getNotificationsWithLinks(Long userId) {
		return commentRepository.findByPostUserId(userId)
			.stream()
			.map(comment -> {
				PostResponseDTO postDTO = new PostResponseDTO(comment.getPost());
				postDTO.setPostTypeKorean(PostType.getKoreanNameByString(comment.getPost().getPostType())); // 한글 변환
				return new CommentResponseDTO(postDTO, 0);
			})
			.collect(Collectors.toList());
	}

	// 작성글 데이터 가져오기
	@Transactional(readOnly = true)
	public List<CommentResponseDTO> getUserPostsWithComments(Long userId) {
		List<Post> posts = postRepository.findByUserUserIdOrderByCreatedAtDesc(userId);

		return posts.stream()
			.map(post -> {
				// Region과 Category 이름 가져오기
				String postTypeKorean;
				if ("PARTICIPATION_REGION".equals(post.getPostType())) {
					postTypeKorean = regionRepository.findById(post.getTargetId())
						.map(Region::getName)
						.orElse("지역 없음");
				} else if ("PARTICIPATION_CATEGORY".equals(post.getPostType())) {
					postTypeKorean = categoryRepository.findById(post.getTargetId())
						.map(Category::getName)
						.orElse("카테고리 없음");
				} else {
					postTypeKorean = PostType.getKoreanNameByString(post.getPostType());
				}

				// PostResponseDTO 생성
				PostResponseDTO postDTO = new PostResponseDTO(post);
				postDTO.setPostTypeKorean(postTypeKorean);

				// 댓글 수 추가
				int commentCount = commentRepository.countCommentsByPostId(post.getPostId());
				return new CommentResponseDTO(postDTO, commentCount);
			})
			.collect(Collectors.toList());
	}


	// Post 객체를 기반으로 PostResponseDTO 생성 (Region/Category 이름 포함)
	private PostResponseDTO createPostResponseDTOWithNames(Post post) {
		String koreanName;

		// postType에 따라 Region/Category 이름 또는 한글 이름 설정
		if ("PARTICIPATION_REGION".equals(post.getPostType())) {
			koreanName = regionRepository.findById(post.getTargetId())
				.map(Region::getName)
				.orElse("지역 없음");
		} else if ("PARTICIPATION_CATEGORY".equals(post.getPostType())) {
			koreanName = categoryRepository.findById(post.getTargetId())
				.map(Category::getName)
				.orElse("카테고리 없음");
		} else {
			koreanName = PostType.getKoreanNameByString(post.getPostType());
		}

		System.out.println("Post Type: " + post.getPostType() + ", Target ID: " + post.getTargetId() + ", Korean Name: " + koreanName);

		PostResponseDTO dto = new PostResponseDTO(post);
		dto.setPostTypeKorean(koreanName);
		return dto;
	}




	// 공지 데이터 가져오기
	@Transactional(readOnly = true)
	public List<CommentResponseDTO> getAnnouncementsWithLinks() {
		List<Post> announcements = postRepository.findAnnouncements();

		return announcements.stream()
			.map(post -> {
				// PostResponseDTO 생성 및 한글 이름 변환
				PostResponseDTO dto = createPostResponseDTOWithNames(post);

				// 댓글 수는 0으로 설정
				return new CommentResponseDTO(dto, 0);
			})
			.collect(Collectors.toList());
	}

}
