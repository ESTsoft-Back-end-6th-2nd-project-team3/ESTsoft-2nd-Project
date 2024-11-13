package com.estsoft.estsoft2ndproject.controller.api;

import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.activityScore.ScoreRequestDTO;
import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.service.MyPageService;
import com.estsoft.estsoft2ndproject.service.PostService;
import com.estsoft.estsoft2ndproject.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage/{userId}")
@RequiredArgsConstructor
public class MyPageController {
	private final MyPageService myPageService;
	private final UserService userService;
	private final PostService postService;

	@GetMapping("/userinfo")
	public ResponseEntity<UserInfoResponseDTO> getMyInfo(@PathVariable(name = "userId") Long userId) {
		return ResponseEntity.ok(myPageService.getMyInfo(userId));
	}

	@PutMapping("/userinfo")
	public ResponseEntity<Void> updateMyInfo(@PathVariable(name = "userId") Long userId, @RequestBody UserInfoRequestDTO userInfo) {
		myPageService.updateMyInfo(userId, userInfo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/objectives")
	public ResponseEntity<List<Objective>> getObjectives(@PathVariable(name = "userId") Long userId) {
		return ResponseEntity.ok(myPageService.getObjectives(userId));
	}

	@PostMapping("/objective")
	public ResponseEntity<Objective> createObjective(@PathVariable(name = "userId") Long userId, @RequestBody ObjectiveRequestDTO objectiveRequestDTO) {
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
	public ResponseEntity<Objective> updateObjective(@PathVariable(name = "userId") Long userId, @PathVariable(name = "objectiveId") Long objectiveId,
		@RequestBody ObjectiveRequestDTO objectiveRequestDTO) {
		return ResponseEntity.ok(myPageService.updateObjective(userId, objectiveId, objectiveRequestDTO));
	}

	@DeleteMapping("/objective/{objectiveId}")
	public ResponseEntity<Void> deleteObjective(@PathVariable(name = "userId") Long userId, @PathVariable(name = "objectiveId") Long objectiveId) {
		myPageService.deleteObjective(userId, objectiveId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostResponseDTO>> getMyPosts(@PathVariable(name = "userId") Long userId) {
		return ResponseEntity.ok(myPageService.getMyPosts(userId));
	}

	@PostMapping("/changeActivityScore")
	public ResponseEntity<Void> changeActivityScore(@PathVariable(name = "userId") Long userId, @RequestBody ScoreRequestDTO scoreRequestDTO) {
		int score = scoreRequestDTO.getScore();
		String reason = scoreRequestDTO.getReason();
		User user = userService.getUserById(userId);

		postService.updateActivityScore(user, score, reason);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/edit-objective")
	public String editObjectivesPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();
		List<Objective> objectives = myPageService.getObjectives(userId);
		model.addAttribute("objectives", objectives);
		return "fragment/edit-objective";
	}
}
