package com.estsoft.estsoft2ndproject.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;

@Controller
@RequestMapping("/post")
public class PostController {

	@GetMapping("/create")
	public String createPost(Model model) {
		model.addAttribute("post", new PostResponseDTO());
		return "post/create-post";
	}
}
