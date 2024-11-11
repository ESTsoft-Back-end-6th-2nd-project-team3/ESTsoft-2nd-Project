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

	@GetMapping("/member/register")
	public String register(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();

		String email = (String)session.getAttribute("email");
		String nickname = (String)session.getAttribute("nickname");
		String profileImageUrl = (String)session.getAttribute("profileImageUrl");

		model.addAttribute("email", email);
		model.addAttribute("nickname", nickname);
		model.addAttribute("profileImageUrl", profileImageUrl);

		return "testHtml/register";
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
	public ResponseEntity<String> delete(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();

		userService.deleteUser(oAuth2User);

		return ResponseEntity.ok("success");
	}

	@GetMapping("/nickname-check")
	public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
		boolean isAvailable = userService.isNicknameAvailable(nickname); // 실제 닉네임 중복 체크 로직
		Map<String, Boolean> response = new HashMap<>();
		response.put("isAvailable", isAvailable);
		return response;
	}

	@PostMapping("/api/upload-profile-image/{userId}")
	public ResponseEntity<Map<String, Object>> uploadProfileImage(
		@PathVariable Long userId,
		@RequestParam("profileImage") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();
		try {
			// 파일 저장 처리 로직 (예: 로컬 파일 시스템 또는 클라우드 스토리지)
			String imageUrl = userService.saveProfileImage(userId, file);

			response.put("success", true);
			response.put("imageUrl", imageUrl);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "이미지 업로드 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
