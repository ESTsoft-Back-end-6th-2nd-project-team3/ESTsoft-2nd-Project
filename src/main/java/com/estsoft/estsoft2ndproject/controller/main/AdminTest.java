package com.estsoft.estsoft2ndproject.controller.main;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.service.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/adminss")
@RequiredArgsConstructor
public class AdminTest {

	private final AdminService adminService;

	@GetMapping("/postssss")
	public String getPostList(
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "postType", required = false) String postType,
		@RequestParam(value = "targetId", required = false) Long targetId,
		@RequestParam(value = "isActive", required = false) Boolean isActive,
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "sort", defaultValue = "latest") String sort, // 기본값 최신순
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "30") int size,
		Model model
	) {
		Page<Map<String, Object>> paginatedPosts = adminService.getFilteredPosts(
			searchType, postType, targetId, isActive, query, sort, page, size
		);

		model.addAttribute("postList", paginatedPosts.getContent());
		model.addAttribute("totalPages", paginatedPosts.getTotalPages());
		model.addAttribute("currentPage", page);
		model.addAttribute("sort", sort); // 현재 정렬 상태 전달

		return "fragment/admin-board-list";
	}
}
