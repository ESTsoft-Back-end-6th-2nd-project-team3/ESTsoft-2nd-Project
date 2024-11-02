package com.estsoft.estsoft2ndproject.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import com.estsoft.estsoft2ndproject.domain.Category;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse;
import com.estsoft.estsoft2ndproject.domain.dto.admin.UserLevelRequest;
import com.estsoft.estsoft2ndproject.domain.dto.admin.UserListResponse;
import com.estsoft.estsoft2ndproject.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	// 회원 목록 조회 GET /admin/users
	@GetMapping("/users")
	public ResponseEntity<List<UserListResponse>> getUsers() {
		List<UserListResponse> users = adminService.getAllUsers()
			.stream()
			.map(user -> new UserListResponse(user.getUserId(), user.getEmail(), user.getNickname(), user.getLevel(),
				user.getActivityScore(), user.getIsActive()))
			.collect(Collectors.toList());
		return ResponseEntity.ok(users);
	}

	// 회원 등급 수정 PUT /admin/user/{user_id}
	@PutMapping("/user/{user_id}")
	public ResponseEntity<User> modifyUserPermissions(@PathVariable("user_id") Long userId,
		@RequestBody UserLevelRequest request) {
		User updatedUser = adminService.updateUserRole(userId, request.getNewLevel());
		return ResponseEntity.ok(updatedUser);
	}

	// 회원 비활성화 PUT /admin/User/{userId}/deactivate
	@PutMapping("/user/{userId}/deactivate")
	public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
		boolean success = adminService.deactivateUser(userId);
		if (success) {
			return ResponseEntity.ok("회원이 비활성화되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 회원을 찾을 수 없습니다.");
		}
	}

	// 게시글 목록 조회 POST /admin/posts
	@PostMapping("/posts")
	public ResponseEntity<List<PostListResponse>> getPosts() {
		List<PostListResponse> posts = adminService.getAllPosts();
		return ResponseEntity.ok(posts);
	}

	// 카테고리 추가 POST /admin/category
	@PostMapping("/category")
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {
		Category newCategory = adminService.addCategory(category);
		return ResponseEntity.ok(newCategory);
	}

	// 카테고리 수정 PUT /admin/category/{category_id}
	@PutMapping("/category/{category_id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Long category_id, @RequestBody Category category) {
		Category updatedCategory = adminService.updateCategoryName(category_id, category.getName());
		return ResponseEntity.ok(updatedCategory);
	}
}

