package com.estsoft.estsoft2ndproject.controller.api;

import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveDTO;
import com.estsoft.estsoft2ndproject.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

	@Autowired
	private MyPageService myPageService;

	@Autowired
	private HttpServletRequest request;

	// 세션에서 user_id 가져와 내 정보 조회
	@GetMapping("/userinfo")
	public UserDTO getMyInfo() {
		HttpSession session = request.getSession();
		Long userId = (Long) session.getAttribute("user_id");

		if (userId == null) {
			throw new RuntimeException("User is not logged in");
		}

		return myPageService.getMyInfoDTO(userId);
	}

	// 세션에서 user_id 가져와 내 정보 수정
	@PutMapping("/userinfo/update")
	public UserDTO updateMyInfo(@RequestBody UserDTO updatedUser) {
		HttpSession session = request.getSession();
		Long userId = (Long) session.getAttribute("user_id");

		if (userId == null) {
			throw new RuntimeException("User is not logged in");
		}

		return myPageService.updateMyInfoDTO(userId, updatedUser);
	}

	// 세션에서 user_id 가져와 내가 작성한 게시글 조회
	@PostMapping("/posts")
	public List<PostDTO> getMyPosts() {
		HttpSession session = request.getSession();
		Long userId = (Long) session.getAttribute("user_id");

		if (userId == null) {
			throw new RuntimeException("User is not logged in");
		}

		return myPageService.getMyPostsDTO(userId);
	}

	// 세션에서 user_id 가져와 목표 조회
	@GetMapping("/objective")
	public List<ObjectiveDTO> getObjective() {
		HttpSession session = request.getSession();
		Long userId = (Long) session.getAttribute("user_id");

		if (userId == null) {
			throw new RuntimeException("User is not logged in");
		}

		return myPageService.getObjectiveDTO(userId);
	}
}
