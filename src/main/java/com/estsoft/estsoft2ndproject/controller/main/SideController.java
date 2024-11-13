package com.estsoft.estsoft2ndproject.controller.main;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SideController {

	private final PostService postService;

	@Autowired
	public SideController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/side-list")
	public String showSideList(Model model) {
		// 오늘의 베스트 게시글
		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		return "fragment/main-page-best";
	}
}
