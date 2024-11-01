package com.estsoft.estsoft2ndproject.controller.api;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	HttpSession session;

	@GetMapping("/member/login")
	public String login() {
		return "login";
	}

	@GetMapping("/member/logout")
	public String logout(@AuthenticationPrincipal OAuth2User oAuth2User) {
		String url = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/logout")
			.queryParam("client_id", oAuth2User.getName())
			.queryParam("logout_redirect_uri", "http://localhost:8080")
			.toUriString();
		return "redirect:" + url;
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/userData")
	@ResponseBody
	public OAuth2User userData(@AuthenticationPrincipal OAuth2User oAuth2User) {
		return oAuth2User;
	}

	@GetMapping("/member/register")
	public String register(Model model) {
		return "register";
	}

	@PostMapping("/member/register")
	public String register(@RequestBody Map<String, Object> body) {
		session.setAttribute("isComplete", "true");
		session.setAttribute(body.get("nickname").toString(), body.get("profileImageUrl").toString());

		return "redirect:/oauth2/authorization/kakao";
	}
}
