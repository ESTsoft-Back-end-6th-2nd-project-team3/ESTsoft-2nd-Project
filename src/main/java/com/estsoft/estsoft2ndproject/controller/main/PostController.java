package com.estsoft.estsoft2ndproject.controller.main;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.PostType;
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
	public String createPost(@RequestParam(required = false) Long postId, Model model) {
		model.addAttribute("postTypes", PostType.values());
		model.addAttribute("categories", postService.getCategoryList());
		model.addAttribute("regions", postService.getRegionList());
		model.addAttribute("userId", 1);
		if (postId == null) {
			model.addAttribute("post", new PostResponseDTO());
		} else {
			Post post = postService.getPostById(postId);
			model.addAttribute("post", new PostResponseDTO(post));
			model.addAttribute("selectedPostType", post.getPostType());
			model.addAttribute("selectedCategory", post.getTargetId());
		}
		return "post/create-post";
	}

	@GetMapping("/{postId}")
	public String getPost(@PathVariable(name = "postId") Long postId, Model model) {
		postService.addViewCount(postId);
		Post post = postService.getPostById(postId);
		Boolean isLiked = postService.getIsLiked(postId, 1L);
		model.addAttribute("post", post);
		model.addAttribute("userId", 1);
		model.addAttribute("PostType", PostType.valueOf(post.getPostType()).getKoreanName());
		model.addAttribute("Category", postService.getCategoryByPostType(post.getPostType(), post.getTargetId()));
		model.addAttribute("isLiked", isLiked);
		return "post/view-post";
	}

	@GetMapping("/category/{targetId}")
	public String getCategory(@RequestParam String postType, @PathVariable Long targetId, Model model) {
		List<Post> postList = postService.getPostsByCategory(postType, targetId);
		String category = postService.getCategoryByPostType(postType, targetId);
		model.addAttribute("posts", postList);
		model.addAttribute("postType", PostType.valueOf(postType).getKoreanName());
		model.addAttribute("category", category);
		return "post/view-post-by-category";
	}

	@GetMapping("/posts")
	public String getPosts(Model model) {
		List<Post> postList = postService.getAllPosts();
		model.addAttribute("posts", postList);
		return "post/view-post-all";
	}

	@GetMapping("/search")
	public String searchPosts(@RequestParam("keyword") String keyword, Model model) {
		List<Post> searchResults = postService.searchPostsByKeyword(keyword);
		model.addAttribute("posts", searchResults);
		model.addAttribute("keyword", keyword);
		return "post/view-search-results";
	}
}
