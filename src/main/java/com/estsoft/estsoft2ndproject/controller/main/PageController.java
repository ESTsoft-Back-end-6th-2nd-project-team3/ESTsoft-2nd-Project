package com.estsoft.estsoft2ndproject.controller.main;

import com.estsoft.estsoft2ndproject.domain.SubMenu;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PageController {

	@GetMapping("/")
	public String menuPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		List<SubMenu> subMenus = new ArrayList<>();
		subMenus.add(new SubMenu("카테고리", Arrays.asList("취미1", "취미2")));
		subMenus.add(new SubMenu("챌린지", null));
		subMenus.add(new SubMenu("지역 친목 게시판", Arrays.asList("강원도", "경기도", "서울특별시")));
		subMenus.add(new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기")));

		if (userDetails != null) {
			User user = userDetails.getUser();

			if (user.getLevel().equals("관리자")) {
				subMenus.add(3, new SubMenu("관리자 메뉴", null));
			}
		}

		model.addAttribute("subMenus", subMenus);

		return "index";
	}
}
