package com.estsoft.estsoft2ndproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.estsoft2ndproject.domain.Category;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse;
import com.estsoft.estsoft2ndproject.domain.dto.admin.UserListResponse;
import com.estsoft.estsoft2ndproject.repository.CategoryRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CategoryRepository categoryRepository;

	@Autowired
	public AdminService(PostRepository postRepository, UserRepository userRepository,
		CategoryRepository categoryRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.categoryRepository = categoryRepository;
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
}
