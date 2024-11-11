package com.estsoft.estsoft2ndproject.controller.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.repository.PostRepository;

@Controller
public class SideController {

	private final PostRepository postRepository;

	@Autowired
	public SideController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("/side-list")
	public String showSideList(Model model) {
		// created_at 기준 내림차순으로 정렬된 활성화된 게시글 20개 가져오기
		List<Post> latestPosts = postRepository.findAllByIsActiveTrue()
			.stream()
			.limit(20)
			.toList();

		// 모델에 데이터를 추가
		model.addAttribute("title", "사이드 목록"); // 페이지 제목
		model.addAttribute("latestPosts", latestPosts); // 최신 게시글 목록

		return "main-page-side"; // side-list.html로 이동
	}
}
