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
		List<SubMenu> subMenus = Arrays.asList(
			new SubMenu("카테고리", Arrays.asList("강원도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시", "대전광역시", "부산광역시", "서울특별시")),
			new SubMenu("챌린지", Arrays.asList("챌린지 아이템 1")),
			new SubMenu("지역 친목 게시판", Arrays.asList("아이템 1", "아이템 2")),
			new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기"))
		);

		// 데이터 확인용 출력
		System.out.println(subMenus);

		model.addAttribute("subMenus", subMenus);
		return "index";
	}
}
