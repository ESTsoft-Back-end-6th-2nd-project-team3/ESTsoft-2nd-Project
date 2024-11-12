package com.estsoft.estsoft2ndproject.controller.main;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.service.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminTest {

	private final AdminService adminService;

	@GetMapping("/posts")
	public String getPostList(@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "30") int size,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "postType", required = false) String postType,
		@RequestParam(value = "targetId", required = false) Long targetId,
		@RequestParam(value = "isActive", required = false) Boolean isActive,
		@RequestParam(value = "query", required = false) String query,
		Model model) {
		// 게시글 전체 목록 가져오기
		List<PostResponseDTO> allPosts = adminService.getPostList();

		// 검색 조건 필터링
		if (searchType != null && query != null && !query.isEmpty()) {
			allPosts = allPosts.stream()
				.filter(post -> {
					if ("title".equals(searchType)) {
						return post.getTitle().contains(query);
					} else if ("user".equals(searchType)) {
						return post.getNickname().contains(query);
					} else if ("content".equals(searchType)) {
						return post.getTitle().contains(query) || post.getDisplayName().contains(query);
					}
					return true;
				}).collect(Collectors.toList());
		}

		if (postType != null) {
			allPosts = allPosts.stream()
				.filter(post -> postType.equals(post.getPostType()))
				.collect(Collectors.toList());
		}

		if (targetId != null) {
			allPosts = allPosts.stream()
				.filter(post -> post.getTargetId().equals(targetId))
				.collect(Collectors.toList());
		}

		if (isActive != null) {
			allPosts = allPosts.stream()
				.filter(post -> post.getIsActive().equals(isActive))
				.collect(Collectors.toList());
		}

		// 페이징 처리
		int startIndex = page * size;
		int endIndex = Math.min(startIndex + size, allPosts.size());
		List<PostResponseDTO> paginatedPosts = allPosts.subList(startIndex, endIndex);

		// 전체 페이지 수 계산
		int totalPages = (int) Math.ceil((double) allPosts.size() / size);

		// 모델에 데이터 추가
		model.addAttribute("postList", paginatedPosts);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);

		return "fragment/admin-board-list";
	}


}
