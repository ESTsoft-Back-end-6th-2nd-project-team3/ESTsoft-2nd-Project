package com.estsoft.estsoft2ndproject.controller.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.repository.PostRepository;

@Controller
public class BoardController {

	private final PostRepository postRepository;

	@Autowired
	public BoardController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("/board")
	public String showBoard(Model model) {
		// created_at 기준 내림차순으로 정렬된 활성화된 게시글 가져오기
		List<Post> allPosts = postRepository.findAllByIsActiveTrue();

		// 첫 번째 "게시글" 목록
		List<Post> latestPosts1 = allPosts.subList(0, Math.min(20, allPosts.size()));
		List<Post> olderPosts1 = allPosts.size() > 20 ? allPosts.subList(20, Math.min(40, allPosts.size())) : List.of();

		// 두 번째 "챌린지" 목록 (예시로 동일한 데이터를 사용)
		List<Post> latestPosts2 = allPosts.subList(0, Math.min(20, allPosts.size()));
		List<Post> olderPosts2 = allPosts.size() > 20 ? allPosts.subList(20, Math.min(40, allPosts.size())) : List.of();

		// 모델에 데이터를 추가
		model.addAttribute("latestPosts1", latestPosts1);
		model.addAttribute("olderPosts1", olderPosts1);
		model.addAttribute("latestPosts2", latestPosts2);
		model.addAttribute("olderPosts2", olderPosts2);

		return "main-page-post"; // main-page-post.html로 이동
	}
}
