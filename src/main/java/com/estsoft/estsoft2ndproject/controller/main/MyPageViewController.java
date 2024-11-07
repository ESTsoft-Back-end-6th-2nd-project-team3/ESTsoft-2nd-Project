package com.estsoft.estsoft2ndproject.controller.main;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.service.ObjectiveService;

@Controller
@RequestMapping("/mypage")
public class MyPageViewController {
	private final ObjectiveService objectiveService;

	public MyPageViewController(ObjectiveService objectiveService) {
		this.objectiveService = objectiveService;
	}

	// @GetMapping("/{userId}")
	// public String showMyPage(@PathVariable Long userId, Model model) {
	// 	int month = LocalDate.now().getMonthValue();
	// 	model.addAttribute("month", month);
	//
	// 	LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
	// 	List<Objective> myObjective = objectiveService.getObjectivesForUserAndMonth(userId, currentMonth);
	// 	model.addAttribute("myObjective", myObjective);
	//
	// 	int progressValue = 50; // 목표 달성률, 필요시 계산 로직 추가
	// 	model.addAttribute("progressValue", progressValue);
	//
	// 	model.addAttribute("userId", userId);
	// 	return "mypage"; // mypage.html 템플릿을 반환
	// }

	@GetMapping("/{userId}")
	public String showMyPage(@PathVariable Long userId, Model model) {
		int month = LocalDate.now().getMonthValue();
		model.addAttribute("month", month);

		// 임시 데이터 추가
		List<Objective> myObjective = List.of(
			new Objective(null, "운동 15회", null, null, null, Date.valueOf("2024-11-01"), false),
			new Objective(null, "독서 3권", null, null, null, Date.valueOf("2024-11-01"), false)
		);
		model.addAttribute("myObjective", myObjective);

		int progressValue = 50; // 임시 달성률 값
		model.addAttribute("progressValue", progressValue);

		model.addAttribute("userId", userId);
		return "mypage";
	}
}
