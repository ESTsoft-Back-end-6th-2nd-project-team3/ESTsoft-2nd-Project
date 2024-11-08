package com.estsoft.estsoft2ndproject.controller.main;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.SubMenu;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.service.ObjectiveService;
import com.estsoft.estsoft2ndproject.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
	private final UserService userService;
	private final ObjectiveService objectiveService;

	public PageController(UserService userService, ObjectiveService objectiveService) {
		this.userService = userService;
		this.objectiveService = objectiveService;
	}

	@GetMapping("/index")
	public String menuPage(Model model) {
		model.addAttribute("subMenus", getSubMenus());
		return "index";
	}

	@GetMapping("/mypage/{userId}")
	public String showMyPage(@PathVariable Long userId, Model model) {
		// 현재 월을 가져와서 추가
		int month = LocalDate.now().getMonthValue();
		model.addAttribute("month", month);

		// 유저 정보와 참여 챌린지 가져오기
		Optional<User> user = userService.getUserWithChallenges(userId);
		user.ifPresentOrElse(
			u -> {
				model.addAttribute("participatedChallenge", u.getAwardedTitle());
			},
			() -> {
				model.addAttribute("participatedChallenge", "참여한 챌린지가 없습니다.");
			}
		);

		// 사용자 목표 리스트 가져오기 (월 기준)
		List<Objective> myObjective = objectiveService.getObjectivesForUserAndMonth(userId,
			LocalDate.now().withDayOfMonth(1));
		if (myObjective.isEmpty()) {
			myObjective = List.of(
				new Objective(null, "운동 15회", null, null, null, Date.valueOf("2024-11-01"), false),
				new Objective(null, "독서 3권", null, null, null, Date.valueOf("2024-11-01"), false)
			);
		}
		model.addAttribute("myObjective", myObjective);

		// 목표 달성률 추가 (임시 값)
		int progressValue = 70;
		model.addAttribute("progressValue", progressValue);

		// 상단 메뉴와 콘텐츠 프래그먼트 설정
		model.addAttribute("subMenus", getSubMenus());
		model.addAttribute("contentFragment", "mypage :: content");

		return "index"; // index.html을 사용
	}

	private List<SubMenu> getSubMenus() {
		String level = "관리자";
		if ("관리자".equals(level)) {
			return Arrays.asList(
				new SubMenu("카테고리", Arrays.asList("음악", "사진", "여행", "게임")),
				new SubMenu("챌린지", Arrays.asList("챌린지 아이템", "특별 챌린지")),
				new SubMenu("지역 친목 게시판",
					Arrays.asList("강원특별자치도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시", "대전광역시", "부산광역시", "서울특별시",
						"세종특별자치시", "울산광역시", "인천광역시", "전라남도", "전북특별자치도", "제주특별자치도", "충청남도", "충청북도")),
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
