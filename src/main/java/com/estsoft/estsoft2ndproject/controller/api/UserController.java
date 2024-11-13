package com.estsoft.estsoft2ndproject.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
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
		return "testHtml/login";
	}

	@GetMapping("/member")
	public String index() {
		return "testHtml/test-index";
	}

	@GetMapping("/userData")
	@ResponseBody
	public OAuth2User userData(@AuthenticationPrincipal OAuth2User oAuth2User) {
		return oAuth2User;
	}

	@PostMapping("/member/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO,
		HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("isComplete", "true");
		session.setAttribute("nickname", registerRequestDTO.getNickname());
		session.setAttribute("profileImageUrl", registerRequestDTO.getProfileImageUrl());
		session.setAttribute("selfIntro", registerRequestDTO.getSelfIntro());
		session.setAttribute("snsLink", registerRequestDTO.getSnsLink());

		return ResponseEntity.ok("success");
	}

	@GetMapping("/member/cancellation")
	public String delete(@AuthenticationPrincipal CustomUserDetails oAuth2User,
		HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();

		userService.deleteUser(oAuth2User);

		return "redirect:/";
	}

	@GetMapping("/nickname-check")
	@ResponseBody
	public Map<String, Boolean> checkNickname(@RequestParam(name = "nickname") String nickname) {
		boolean isAvailable = userService.isNicknameAvailable(nickname);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isAvailable", isAvailable);
		return response;
	}

	@GetMapping("/member/{userId}/authority/addAdmin")
	public ResponseEntity<String> addAdmin(@PathVariable(name = "userId") Long userId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userService.getUserById(userId);

		if (userDetails.getUser().getLevel().equals("관리자")) {
			userService.updateUserLevel(user, "관리자");

			return ResponseEntity.ok("success");
		}

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
	}

	@GetMapping("/member/{userId}/authority/deleteAdmin")
	public ResponseEntity<String> deleteAdmin(@PathVariable(name = "userId") Long userId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userService.getUserById(userId);

		if (userDetails.getUser().getLevel().equals("관리자")) {
			userService.updateUserLevel(user, "씨앗");

			return ResponseEntity.ok("success");
		}

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
	}
}
