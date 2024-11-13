package com.estsoft.estsoft2ndproject.controller.api;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.service.MyPageService;

@Controller
public class ObjectiveController {

	private final MyPageService myPageService;

	public ObjectiveController(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping("/objectives")
	public String objectivesPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();

		// DB에서 유저의 목표 데이터를 가져오기
		List<Objective> objectives = myPageService.getObjectives(userId);

		model.addAttribute("userId", userId);
		model.addAttribute("objectives", objectives);
		return "fragment/edit-objective"; // HTML 파일 이름
	}
}