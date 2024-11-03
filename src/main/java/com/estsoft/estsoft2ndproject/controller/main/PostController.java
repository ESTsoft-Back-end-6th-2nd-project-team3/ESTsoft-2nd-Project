package com.estsoft.estsoft2ndproject.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.service.PostService;

@Controller
@RequestMapping("/post")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/create")
	public String createPost(Model model) {
		model.addAttribute("post", new PostResponseDTO());
		return "post/create-post";
	}

	@GetMapping("/{postId}")
	public String getPost(@PathVariable Long postId, Model model) {
		Post post = postService.getPostById(postId);

		model.addAttribute("post", post);
		return "post/view-post";
	}
}
