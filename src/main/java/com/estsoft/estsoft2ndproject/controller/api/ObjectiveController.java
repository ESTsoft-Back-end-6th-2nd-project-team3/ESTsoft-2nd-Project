package com.estsoft.estsoft2ndproject.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;

@Controller
public class ObjectiveController {

	@GetMapping("/objectives")
	public String objectivesPage(Model model, CustomUserDetails userDetails) {
		model.addAttribute("userId", userDetails.getUser().getUserId()); // 로그인된 유저 ID 전달
		return "fragment/edit-objective"; // HTML 파일 이름
	}
}