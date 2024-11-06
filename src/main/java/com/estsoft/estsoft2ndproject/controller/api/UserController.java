package com.estsoft.estsoft2ndproject.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.estsoft.estsoft2ndproject.domain.dto.user.RegisterRequestDTO;
import com.estsoft.estsoft2ndproject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/member/login")
	public String login() {
		return "login";
	}

	@GetMapping("/member")
	public String index() {
		return "test-index";
	}

	@GetMapping("/userData")
	@ResponseBody
	public OAuth2User userData(@AuthenticationPrincipal OAuth2User oAuth2User) {
		return oAuth2User;
	}

	@GetMapping("/member/register")
	public String register(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();

		String email = (String)session.getAttribute("email");
		String nickname = (String)session.getAttribute("nickname");
		String profileImageUrl = (String)session.getAttribute("profileImageUrl");

		model.addAttribute("email", email);
		model.addAttribute("nickname", nickname);
		model.addAttribute("profileImageUrl", profileImageUrl);

		return "register";
	}

	@PostMapping("/member/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("isComplete", "true");
		session.setAttribute("nickname", registerRequestDTO.getNickname());
		session.setAttribute("profileImageUrl", registerRequestDTO.getProfileImageUrl());
		session.setAttribute("selfIntro", registerRequestDTO.getSelfIntro());
		session.setAttribute("snsLink", registerRequestDTO.getSnsLink());

		return ResponseEntity.ok("success");
	}

	@GetMapping("/member/cancellation")
	public ResponseEntity<String> delete(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();

		userService.deleteUser(oAuth2User);

		return ResponseEntity.ok("success");
	}
}
