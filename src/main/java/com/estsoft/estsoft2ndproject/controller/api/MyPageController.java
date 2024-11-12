package com.estsoft.estsoft2ndproject.controller.api;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.service.MyPageService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mypage/{userId}")
@RequiredArgsConstructor
public class MyPageController {
	private final MyPageService myPageService;

	@GetMapping("/userinfo")
	public ResponseEntity<UserInfoResponseDTO> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();
		return ResponseEntity.ok(myPageService.getMyInfo(userId));
	}

	@PutMapping("/userinfo")
	public ResponseEntity<Void> updateMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody UserInfoResponseDTO userInfo) {
		Long userId = userDetails.getUser().getUserId();
		myPageService.updateMyInfo(userId, userInfo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/objectives")
	public ResponseEntity<List<Objective>> getObjectives(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();
		return ResponseEntity.ok(myPageService.getObjectives(userId));
	}

	@PostMapping("/objective")
	public ResponseEntity<Objective> createObjective(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody ObjectiveRequestDTO objectiveRequestDTO) {
		Long userId = userDetails.getUser().getUserId();
		return ResponseEntity.ok(myPageService.createObjective(userId, objectiveRequestDTO));
	}

	@PostMapping("/objectives")
	public ResponseEntity<Void> saveObjectives(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody List<ObjectiveRequestDTO> objectiveRequestDTOs) {
		Long userId = userDetails.getUser().getUserId();
		myPageService.saveOrUpdateObjectives(userId, objectiveRequestDTOs);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/objective/{objectiveId}")
	public ResponseEntity<Objective> updateObjective(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long objectiveId,
		@RequestBody ObjectiveRequestDTO objectiveRequestDTO) {
		Long userId = userDetails.getUser().getUserId();
		return ResponseEntity.ok(myPageService.updateObjective(userId, objectiveId, objectiveRequestDTO));
	}

	@DeleteMapping("/objective/{objectiveId}")
	public ResponseEntity<Void> deleteObjective(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long objectiveId) {
		Long userId = userDetails.getUser().getUserId();
		myPageService.deleteObjective(userId, objectiveId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostResponseDTO>> getMyPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();
		return ResponseEntity.ok(myPageService.getMyPosts(userId));
	}

	@GetMapping("/edit-objective")
	public String editObjectivesPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();
		List<Objective> objectives = myPageService.getObjectives(userId);
		model.addAttribute("objectives", objectives);
		return "fragment/edit-objective";
	}
}
