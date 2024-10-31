package com.estsoft.estsoft2ndproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

@Service
public class AdminService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public AdminService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	// 회원 목록 조회
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.findAll();
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

	// 게시글 목록 조회
	@Transactional(readOnly = true)
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	// 카테고리 수정
	@Transactional
	public Post updatePostCategory(Long postId, String newCategory) {
		Optional<Post> postOptional = postRepository.findById(postId);
		if (postOptional.isPresent()) {
			Post post = postOptional.get();
			post.setPostType(newCategory);
			return postRepository.save(post);
		} else {
			throw new IllegalArgumentException("해당 ID의 게시글이 존재하지 않습니다.");
		}
	}
}
