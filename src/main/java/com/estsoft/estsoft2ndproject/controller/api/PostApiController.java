package com.estsoft.estsoft2ndproject.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostApiController {
	private final PostService postService;

	public PostApiController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/{userId}/create")
	public ResponseEntity<Map<String, Long>> createPost(@RequestBody PostRequestDTO postRequestDTO,
		@PathVariable Long userId) {
		Post post = postService.createPost(postRequestDTO, userId);
		Map<String, Long> response = new HashMap<>();
		response.put("postId", post.getPostId()); // 생성된 postId를 JSON 응답으로 반환
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {
		Post post = postService.getPostById(postId);
		return ResponseEntity.ok(new PostResponseDTO(post));
	}

	@PutMapping("/{postId}")
	public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId,
		@RequestBody PostRequestDTO postRequestDTO) {
		Post post = postService.updatePost(postId, postRequestDTO);
		return ResponseEntity.ok(new PostResponseDTO(post));
	}
}
