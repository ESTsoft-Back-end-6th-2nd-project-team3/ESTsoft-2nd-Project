package com.estsoft.estsoft2ndproject.controller.main;

import com.estsoft.estsoft2ndproject.domain.SubMenu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class PageController {

	@GetMapping("/index")
	public String menuPage(Model model) {
		List<SubMenu> subMenus;

		String level = "관리자";

		// 조건에 따라 서브메뉴 구성
		if ("관리자".equals(level)) {
			subMenus = Arrays.asList(
				new SubMenu("카테고리", Arrays.asList("취미1", "취미2")),
				new SubMenu("챌린지", Arrays.asList("챌린지 아이템 1", "특별 챌린지")),
				new SubMenu("지역 친목 게시판", Arrays.asList("강원도", "경기도", "서울특별시")),
				new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기")),
				new SubMenu("관리자 메뉴", Arrays.asList("사용자 관리", "사이트 설정"))
			);
		} else {
			subMenus = Arrays.asList(
				new SubMenu("카테고리", Arrays.asList("취미1", "취미2")),
				new SubMenu("챌린지", Arrays.asList("챌린지 아이템 1")),
				new SubMenu("지역 친목 게시판", Arrays.asList("강원도", "경기도", "서울특별시")),
				new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기"))
			);
		}

		model.addAttribute("subMenus", subMenus);
		return "index";
	}
}
