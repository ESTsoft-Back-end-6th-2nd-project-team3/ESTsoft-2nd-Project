package com.estsoft.estsoft2ndproject.service;

import java.util.List;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Comment;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.exception.PostNotFoundException;
import com.estsoft.estsoft2ndproject.repository.CommentRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

@Service
public class CommentService {
	private final PostService postService;
	CommentRepository commentRepository;
	PostRepository postRepository;
	UserRepository userRepository;

	public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository,
		PostService postService) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.postService = postService;
	}

	public List<Comment> getCommentsByPostId(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		return commentRepository.findByPostAndIsActive(post, true);
	}

	@Transactional
	public Comment createComment(CommentRequestDTO commentRequestDTO, Long postId, CustomUserDetails oAuth2User) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		User user = oAuth2User.getUser();

		postService.updateActivityScore(user, 1, "댓글 작성");

		return commentRepository.save(commentRequestDTO.toEntity(post, user));
	}

	@Transactional
	public Comment createComment(CommentRequestDTO commentRequestDTO, Long postId, Long commentId, CustomUserDetails oAuth2User) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		User user = oAuth2User.getUser();
		Comment parentComment = commentRepository.findById(commentId).orElse(null);

		postService.updateActivityScore(user, 1, "댓글 작성");

		return commentRepository.save(commentRequestDTO.toEntity(post, user, parentComment));
	}

	@Transactional
	public Comment updateComment(Long commentId, CommentRequestDTO commentRequestDTO) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
		comment.setContent(commentRequestDTO.getContent());
		return commentRepository.save(comment);
	}

	@Transactional
	public void deleteComment(Long commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
		comment.setIsActive(false);
		User commentAuthor = comment.getUser();
		postService.updateActivityScore(commentAuthor, -1, "댓글 삭제");
		commentRepository.save(comment);
	}

	public Integer getCommentCountByPostId(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		return commentRepository.countByPostAndIsActive(post, true);
	}

	public List<CommentResponseDTO> getCommentsDetailByPostId(Long postId) {
		return getCommentsByPostId(postId)
			.stream()
			.map(comment -> {
				CommentResponseDTO commentResponseDTO = new CommentResponseDTO(comment);
				commentResponseDTO.setNickname(comment.getUser().getNickname());
				commentResponseDTO.setProfileUrl(comment.getUser().getProfileImageUrl());
				return commentResponseDTO;
			})
			.toList();
	}
}
