package com.estsoft.estsoft2ndproject.controller.main;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.SubMenu;
import com.estsoft.estsoft2ndproject.service.ObjectiveService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

	private final ObjectiveService objectiveService;

	public PageController(ObjectiveService objectiveService) {
		this.objectiveService = objectiveService;
	}

	@GetMapping("/index")
	public String menuPage(Model model) {
		model.addAttribute("subMenus", getSubMenus());
		return "index";
	}

	@GetMapping("/mypage/{userId}")
	public String showMyPage(@PathVariable Long userId, Model model) {
		int month = LocalDate.now().getMonthValue();
		model.addAttribute("month", month);

		List<Objective> myObjective = objectiveService.getObjectivesForUserAndMonth(userId,
			LocalDate.now().withDayOfMonth(1));

		if (myObjective.isEmpty()) {
			myObjective = List.of(
				new Objective(null, "운동 15회", null, null, null, Date.valueOf("2024-11-01"), false),
				new Objective(null, "독서 3권", null, null, null, Date.valueOf("2024-11-01"), false)
			);
		}
		model.addAttribute("myObjective", myObjective);

		int progressValue = 50;
		model.addAttribute("progressValue", progressValue);

		model.addAttribute("subMenus", getSubMenus());
		model.addAttribute("contentFragment", "mypage :: content");

		return "index"; // index.html을 사용
	}

	private List<SubMenu> getSubMenus() {
		String level = "관리자";
		if ("관리자".equals(level)) {
			return Arrays.asList(
				new SubMenu("카테고리", Arrays.asList("취미1", "취미2")),
				new SubMenu("챌린지", Arrays.asList("챌린지 아이템 1", "특별 챌린지")),
				new SubMenu("지역 친목 게시판", Arrays.asList("강원도", "경기도", "서울특별시")),
				new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기")),
				new SubMenu("관리자 메뉴", Arrays.asList("사용자 관리", "사이트 설정"))
			);
		} else {
			return Arrays.asList(
				new SubMenu("카테고리", Arrays.asList("취미1", "취미2")),
				new SubMenu("챌린지", Arrays.asList("챌린지 아이템 1")),
				new SubMenu("지역 친목 게시판", Arrays.asList("강원도", "경기도", "서울특별시")),
				new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기"))
			);
		}
	}
}
