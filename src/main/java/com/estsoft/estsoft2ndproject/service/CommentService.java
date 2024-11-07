package com.estsoft.estsoft2ndproject.service;

import java.util.List;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Comment;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentRequestDTO;
import com.estsoft.estsoft2ndproject.exception.PostNotFoundException;
import com.estsoft.estsoft2ndproject.repository.CommentRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

@Service
public class CommentService {
	CommentRepository commentRepository;
	PostRepository postRepository;
	UserRepository userRepository;

	public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	public List<Comment> getCommentsByPostId(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		return commentRepository.findByPost(post);
	}

	@Transactional
	public Comment createComment(CommentRequestDTO commentRequestDTO, Long postId, OAuth2User oAuth2User) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		User user = userRepository.findByPii(oAuth2User.getName());

		return commentRepository.save(commentRequestDTO.toEntity(post, user));
	}

	@Transactional
	public Comment createComment(CommentRequestDTO commentRequestDTO, Long postId, Long commentId, OAuth2User oAuth2User) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		User user = userRepository.findByPii(oAuth2User.getName());
		Comment parentComment = commentRepository.findById(commentId).orElse(null);

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
		commentRepository.save(comment);
	}
}
