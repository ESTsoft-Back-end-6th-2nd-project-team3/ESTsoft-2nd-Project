package com.estsoft.estsoft2ndproject.controller.api;

import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.service.MyPageService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage/{userId}")
@RequiredArgsConstructor
public class MyPageController {
	private final MyPageService myPageService;

	@GetMapping("/userinfo")
	public ResponseEntity<UserInfoResponseDTO> getMyInfo(@PathVariable Long userId) {
		return ResponseEntity.ok(myPageService.getMyInfo(userId));
	}

	@PutMapping("/userinfo")
	public ResponseEntity<Void> updateMyInfo(@PathVariable Long userId, @RequestBody UserInfoResponseDTO userInfo) {
		myPageService.updateMyInfo(userId, userInfo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/objectives")
	public ResponseEntity<List<Objective>> getObjectives(@PathVariable Long userId) {
		return ResponseEntity.ok(myPageService.getObjectives(userId));
	}

	@PostMapping("/objective")
	public ResponseEntity<Objective> createObjective(@PathVariable Long userId, @RequestBody ObjectiveRequestDTO objectiveRequestDTO) {
		return ResponseEntity.ok(myPageService.createObjective(userId, objectiveRequestDTO));
	}

	@PutMapping("/objective/{objectiveId}")
	public ResponseEntity<Objective> updateObjective(@PathVariable Long userId, @PathVariable Long objectiveId, @RequestBody ObjectiveRequestDTO objectiveRequestDTO) {
		return ResponseEntity.ok(myPageService.updateObjective(userId, objectiveId, objectiveRequestDTO));
	}

	@DeleteMapping("/objective/{objectiveId}")
	public ResponseEntity<Void> deleteObjective(@PathVariable Long userId, @PathVariable Long objectiveId) {
		myPageService.deleteObjective(userId, objectiveId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostResponseDTO>> getMyPosts(@PathVariable Long userId) {
		return ResponseEntity.ok(myPageService.getMyPosts(userId));
	}
}
