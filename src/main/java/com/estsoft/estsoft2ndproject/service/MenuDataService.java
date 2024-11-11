package com.estsoft.estsoft2ndproject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.post.CommentResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.repository.CommentRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;

@Service
public class MenuDataService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	public MenuDataService(PostRepository postRepository, CommentRepository commentRepository) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	// 알림 데이터 가져오기
	@Transactional(readOnly = true)
	public List<CommentResponseDTO> getNotificationsWithLinks(Long userId) {
		// 댓글 정보를 통해 알림 데이터 생성
		return commentRepository.findByPostUserId(userId)
			.stream()
			.map(comment -> {
				PostResponseDTO postDTO = new PostResponseDTO(comment.getPost());
				return new CommentResponseDTO(postDTO, 0); // 댓글 수는 0으로 설정
			})
			.collect(Collectors.toList());
	}


	@Transactional(readOnly = true)
	public List<CommentResponseDTO> getUserPostsWithComments(Long userId) {
		// 기존 방식으로 PostResponseDTO 리스트 생성
		List<PostResponseDTO> postResponseDTOs = postRepository.findByUserUserIdOrderByCreatedAtDesc(userId)
			.stream()
			.map(PostResponseDTO::new)
			.toList();

		// 댓글 수를 추가하여 NewPostResponseDTO로 변환
		return postResponseDTOs.stream()
			.map(postDTO -> {
				int commentCount = commentRepository.countCommentsByPostId(postDTO.getPostId()); // 댓글 수 계산
				return new CommentResponseDTO(postDTO, commentCount);
			})
			.collect(Collectors.toList());
	}

	// 공지 데이터 가져오기
	@Transactional(readOnly = true)
	public List<CommentResponseDTO> getAnnouncementsWithLinks() {
		List<Post> announcements = postRepository.findAnnouncements();
		return announcements.stream()
			.map(post -> new CommentResponseDTO(new PostResponseDTO(post), 0)) // 댓글 수는 0으로 설정
			.collect(Collectors.toList());
	}

}
