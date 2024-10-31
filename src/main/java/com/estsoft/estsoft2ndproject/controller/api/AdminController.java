package com.estsoft.estsoft2ndproject.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.admin.CategoryRequest;
import com.estsoft.estsoft2ndproject.domain.dto.admin.UserLevelRequest;
import com.estsoft.estsoft2ndproject.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	// 회원 목록 조회 GET /admin/users
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	// 회원 등급 수정 PUT /admin/user/{user_id}
	@PutMapping("/user/{user_id}")
	public ResponseEntity<User> modifyUserPermissions(@PathVariable("user_id") Long userId,
		@RequestBody UserLevelRequest request) {
		User updatedUser = adminService.updateUserRole(userId, request.getNewLevel());
		return ResponseEntity.ok(updatedUser);
	}

	// 게시글 목록 조회 POST /admin/posts
	@PostMapping("/posts")
	public ResponseEntity<List<Post>> getPosts() {
		List<Post> posts = adminService.getAllPosts();
		return ResponseEntity.ok(posts);
	}

	// 카테고리 수정 PUT /admin/category
	@PutMapping("/category")
	public ResponseEntity<Post> updateCategory(@RequestParam Long postId, @RequestBody CategoryRequest request) {
		Post updatedPost = adminService.updatePostCategory(postId, request.getNewCategory());
		return ResponseEntity.ok(updatedPost);
	}
}

