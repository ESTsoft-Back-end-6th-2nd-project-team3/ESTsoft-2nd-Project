package com.estsoft.estsoft2ndproject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Comment;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.post.NewPostResponseDTO;
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
	public List<String> getNotifications(Long userId) {
		List<Comment> comments = postRepository.findCommentsByUserId(userId);
		return comments.stream()
			.map(comment -> "게시글 '" + comment.getPost().getTitle() + "'에 댓글이 달렸습니다: " + comment.getContent())
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<NewPostResponseDTO> getUserPostsWithComments(Long userId) {
		// 기존 방식으로 PostResponseDTO 리스트 생성
		List<PostResponseDTO> postResponseDTOs = postRepository.findByUserUserIdOrderByCreatedAtDesc(userId)
			.stream()
			.map(PostResponseDTO::new)
			.collect(Collectors.toList());

		// 댓글 수를 추가하여 NewPostResponseDTO로 변환
		return postResponseDTOs.stream()
			.map(postDTO -> {
				int commentCount = commentRepository.countCommentsByPostId(postDTO.getPostId()); // 댓글 수 계산
				return new NewPostResponseDTO(postDTO, commentCount);
			})
			.collect(Collectors.toList());
	}

	// 공지 데이터 가져오기
	@Transactional(readOnly = true)
	public List<String> getAnnouncements() {
		List<Post> announcements = postRepository.findAnnouncements();
		return announcements.stream()
			.map(announcement -> "공지: " + announcement.getTitle())
			.collect(Collectors.toList());
	}
}
