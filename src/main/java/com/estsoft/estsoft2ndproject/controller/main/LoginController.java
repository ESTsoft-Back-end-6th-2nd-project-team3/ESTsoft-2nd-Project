package com.estsoft.estsoft2ndproject.controller.main;

import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.repository.UserRepository;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final UserRepository userRepository;

	@GetMapping("/main-page-signin")
	public String loginPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails != null) {
			// 로그인된 사용자의 정보를 가져옵니다.
			User user = userRepository.findById(userDetails.getUser().getUserId())
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

			// 로그인된 사용자 정보를 모델에 추가
			model.addAttribute("isLoggedIn", true);
			model.addAttribute("userNickname", user.getNickname());
			model.addAttribute("userLevel", user.getLevel());
			model.addAttribute("userActivityScore", user.getActivityScore());
			model.addAttribute("profileImageUrl", user.getProfileImageUrl());
		} else {
			// 비로그인 상태일 경우
			model.addAttribute("isLoggedIn", false);
		}
		return "fragment/main-page-signin"; // login.html로 이동
	}
}
