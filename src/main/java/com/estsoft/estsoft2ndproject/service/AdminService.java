package com.estsoft.estsoft2ndproject.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Category;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.PostType;
import com.estsoft.estsoft2ndproject.domain.Region;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse;
import com.estsoft.estsoft2ndproject.domain.dto.admin.UserListResponse;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.repository.CategoryRepository;
import com.estsoft.estsoft2ndproject.repository.CommentRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.RegionRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;
	private final RegionRepository regionRepository;
	private final CommentRepository commentRepository;

	@Autowired
	public AdminService(PostRepository postRepository, UserRepository userRepository,
		CategoryRepository categoryRepository, RegionRepository regionRepository, CommentRepository commentRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.categoryRepository = categoryRepository;
		this.regionRepository = regionRepository;
		this.commentRepository = commentRepository;
	}

	// 회원 목록 조회
	@Transactional(readOnly = true)
	public List<UserListResponse> getAllUsers() {
		return userRepository.findAll()
			.stream()
			.map(user -> new UserListResponse(user.getUserId(), user.getEmail(), user.getNickname(), user.getLevel(),
				user.getActivityScore(), user.getIsActive()))
			.collect(Collectors.toList());
	}

	// 회원 등급 수정
	@Transactional
	public User updateUserRole(Long userId, String newLevel) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setLevel(newLevel);
			return userRepository.save(user);
		} else {
			throw new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.");
		}
	}

	// 회원 비활성화
	@Transactional
	public boolean deactivateUser(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setIsActive(false);
			userRepository.save(user);
			return true;
		} else {
			return false;
		}
	}

	// 게시글 목록 조회
	@Transactional(readOnly = true)
	public List<PostListResponse> getAllPosts() {
		return postRepository.findPostListWithCategoryAndComments();
	}

	// 카테고리 추가
	@Transactional
	public Category addCategory(Category category) {
		return categoryRepository.save(category);
	}

	// 카테고리 수정
	@Transactional
	public Category updateCategoryName(Long categoryId, String newCategoryName) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
		category.setName(newCategoryName);
		return categoryRepository.save(category);
	}

	@Transactional(readOnly = true)
	public List<PostResponseDTO> getPostList() {
		// 모든 게시글 가져오기
		List<Post> posts = postRepository.findAll(); // Post 엔터티를 가져옴

		// 각 게시글에 대해 필요한 정보 추출 및 DTO 변환
		return posts.stream().map(post -> {
			// 게시글 작성자 닉네임 가져오기
			String nickname = post.getUser().getNickname();

			// PostResponseDTO 생성
			return new PostResponseDTO(
				post.getPostId(),
				post.getTitle(),
				post.getPostType(),
				post.getTargetId(),
				nickname, // 닉네임 추가
				post.getViewCount(),
				post.getCreatedAt(),
				post.getIsActive() // 활성화 여부 추가
			);
		}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<Map<String, Object>> getFilteredPosts(
		String searchType,
		String postType,
		Long targetId,
		Boolean isActive,
		String query,
		String sort,
		int page,
		int size
	) {
		Sort sortOption = Sort.by(Sort.Direction.DESC, "createdAt"); // 기본값: 최신순
		if ("views".equals(sort)) {
			sortOption = Sort.by(Sort.Direction.DESC, "viewCount");
		}

		Pageable pageable = PageRequest.of(page, size, sortOption);

		Page<Post> posts = postRepository.findFilteredPosts(searchType, postType, targetId, isActive, query, pageable);

		return posts.map(post -> {
			Map<String, Object> postMap = new HashMap<>();
			PostResponseDTO dto = new PostResponseDTO(post);

			int commentCount = commentRepository.countCommentsByPostId(post.getPostId());
			String categoryName = (post.getTargetId() != null)
				? categoryRepository.findById(post.getTargetId()).map(Category::getName).orElse("참여")
				: "참여";

			String nickname = post.getUser().getNickname();

			// PostType 한글 변환
			String postTypeDisplayName = PostType.getKoreanNameByString(post.getPostType());

			postMap.put("dto", dto);
			postMap.put("commentCount", commentCount);
			postMap.put("displayName", categoryName);
			postMap.put("nickname", nickname);
			postMap.put("postTypeDisplayName", postTypeDisplayName); // 한글 이름 추가

			return postMap;
		});
	}

	@Transactional
	public boolean toggleActiveStatus(Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Post not found"));

		post.setIsActive(!post.getIsActive()); // 상태 변경
		postRepository.save(post);

		return post.getIsActive(); // 변경된 상태 반환
	}

}
