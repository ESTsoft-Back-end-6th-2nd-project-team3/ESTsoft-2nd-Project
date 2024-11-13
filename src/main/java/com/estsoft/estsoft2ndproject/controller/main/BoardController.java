package com.estsoft.estsoft2ndproject.controller.main;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.service.PostService;

@Controller
public class BoardController {

	private final PostService postService;

	@Autowired
	public BoardController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/board")
	public String showBoard(Model model) {
		try {
			// 모든 활성 게시글 가져오기
			List<PostResponseDTO> allPostsDTO = postService.getAllActivePostsAsDTO();
			if (allPostsDTO == null || allPostsDTO.isEmpty()) {
				throw new Exception("활성 게시글이 없습니다.");
			}

			// 게시글 필터링
			List<PostResponseDTO> regionAndCategoryPosts = postService.filterPostsByType(
				allPostsDTO, List.of("PARTICIPATION_CATEGORY", "PARTICIPATION_REGION")
			);
			List<PostResponseDTO> challengePosts = postService.filterPostsByType(
				allPostsDTO, List.of("PARTICIPATION_CHALLENGE", "GENERATION_CHALLENGE")
			);

			// 필터링된 리스트가 null인 경우 기본값 설정
			regionAndCategoryPosts = regionAndCategoryPosts != null ? regionAndCategoryPosts : Collections.emptyList();
			challengePosts = challengePosts != null ? challengePosts : Collections.emptyList();

			// 게시글 나누기
			model.addAttribute("latestPosts", getSubList(regionAndCategoryPosts, 0, 20));
			model.addAttribute("olderPosts", getSubList(regionAndCategoryPosts, 20, 40));

			// 챌린지 게시글 나누기
			model.addAttribute("latestChallenges", getSubList(challengePosts, 0, 20));
			model.addAttribute("olderChallenges", getSubList(challengePosts, 20, 40));
		} catch (Exception e) {
			// 오류 처리 및 기본값 설정
			model.addAttribute("latestPosts", Collections.emptyList());
			model.addAttribute("olderPosts", Collections.emptyList());
			model.addAttribute("latestChallenges", Collections.emptyList());
			model.addAttribute("olderChallenges", Collections.emptyList());
			model.addAttribute("error", "게시판 데이터를 로드하는 중 오류가 발생했습니다.");
			e.printStackTrace(); // 로그 남기기
		}

		return "fragment/board-list";
	}

	/**
	 * 리스트의 부분 리스트를 안전하게 반환하는 유틸리티 메서드.
	 *
	 * @param list 대상 리스트
	 * @param start 시작 인덱스
	 * @param end 종료 인덱스
	 * @return 부분 리스트 또는 빈 리스트
	 */
	private List<PostResponseDTO> getSubList(List<PostResponseDTO> list, int start, int end) {
		if (list == null || list.isEmpty() || start >= list.size()) {
			return Collections.emptyList();
		}
		// 안전한 끝 인덱스 계산
		int toIndex = Math.min(end, list.size());
		return list.subList(start, toIndex);
	}
}
