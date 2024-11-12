package com.estsoft.estsoft2ndproject.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.post.LikeRequestDTO;
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
		response.put("postId", post.getPostId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {
		Post post = postService.getPostById(postId);
		Boolean isLiked = postService.getIsLiked(postId, 1L);
		PostResponseDTO postResponseDTO = new PostResponseDTO(post);
		postResponseDTO.setIsLiked(isLiked);
		return ResponseEntity.ok(postResponseDTO);
	}

	@PutMapping("/{postId}")
	public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId,
		@RequestBody PostRequestDTO postRequestDTO) {
		Post post = postService.updatePost(postId, postRequestDTO);
		return ResponseEntity.ok(new PostResponseDTO(post));
	}

	@PostMapping("/category/{targetId}")
	public ResponseEntity<List<PostResponseDTO>> getPostsByCategory(@RequestParam String postType,
		@PathVariable Long targetId) {
		List<PostResponseDTO> PostResponseDTOList = postService.getPostsByCategory(postType, targetId).stream()
			.map(PostResponseDTO::new)
			.toList();
		return ResponseEntity.ok(PostResponseDTOList);
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<PostResponseDTO> deletePost(@PathVariable Long postId) {
		postService.deletePost(postId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/like")
	public ResponseEntity<?> likePost(@RequestBody LikeRequestDTO request) {
		if (!postService.getIsLiked(request.getTargetId(), request.getUserId())) {
			postService.Like(request.getTargetId(), request.getUserId());
		}
		int likeCount = postService.getPostById(request.getTargetId()).getLikeCount();
		return ResponseEntity.ok((Map.of("success", true, "likeCount", likeCount)));

	}

	@PostMapping("/unlike")
	public ResponseEntity<?> unlikePost(@RequestBody LikeRequestDTO request) {
		if (postService.getIsLiked(request.getTargetId(), request.getUserId())) {
			postService.UnLike(request.getTargetId(), request.getUserId());
		}
		int likeCount = postService.getPostById(request.getTargetId()).getLikeCount();
		return ResponseEntity.ok((Map.of("success", true, "likeCount", likeCount)));
	}
}
