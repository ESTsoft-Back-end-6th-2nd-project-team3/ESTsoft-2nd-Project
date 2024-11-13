package com.estsoft.estsoft2ndproject.controller.main;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.service.MyPageService;

@Controller
public class ObjectTest {
	private final MyPageService myPageService;

	public ObjectTest(MyPageService myPageService) {
		this.myPageService = myPageService;
	}

	@GetMapping("/objectives/stats")
	public String getObjectiveStats(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			throw new RuntimeException("로그인이 필요합니다.");
		}

		// 로그인된 사용자 ID 가져오기
		Long userId = userDetails.getUser().getUserId();

		// 서비스 호출
		List<Map<String, Object>> objectivesData = myPageService.getMonthlyCompletionStats(userId);

		// 데이터를 모델에 추가
		model.addAttribute("objectivesData", objectivesData);

		return "objectives_stats"; // objective-stats.html로 데이터 전달
	}
}
