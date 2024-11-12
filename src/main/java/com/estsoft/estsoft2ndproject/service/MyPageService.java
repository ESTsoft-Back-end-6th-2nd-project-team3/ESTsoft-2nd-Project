package com.estsoft.estsoft2ndproject.service;

import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.repository.ObjectiveRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
	private final UserRepository userRepository;
	private final ObjectiveRepository objectiveRepository;
	private final PostRepository postRepository;

	public UserInfoResponseDTO getMyInfo(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return new UserInfoResponseDTO(user.getUserId(), user.getEmail(), user.getNickname(), user.getProfileImageUrl(),
			user.getAwardedTitle(), user.getSelfIntro(), user.getSnsLink(), user.getActivityScore());
	}

	public void updateMyInfo(Long userId, UserInfoRequestDTO userInfo) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		user.updateBuilder()
			.nickname(userInfo.getNickname())
			.profileImageUrl(userInfo.getProfileImageUrl())
			.selfIntro(userInfo.getSelfIntro())
			.snsLink(userInfo.getSnsLink())
			.build();

		userRepository.save(user);
	}

	public List<Objective> getObjectives(Long userId) {
		return objectiveRepository.findByUser_UserId(userId);
	}

	public Objective createObjective(Long userId, ObjectiveRequestDTO objectiveRequestDTO) {
		Objective objective = new Objective();
		objective.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
		objective.setContent(objectiveRequestDTO.getContent());
		objective.setIsCompleted(objectiveRequestDTO.getIsCompleted());
		objective.setObjectiveYearMonth(objectiveRequestDTO.getObjectiveYearMonth());
		return objectiveRepository.save(objective);
	}

	public Objective updateObjective(Long userId, Long objectiveId, ObjectiveRequestDTO objectiveRequestDTO) {
		Objective objective = objectiveRepository.findById(objectiveId).orElseThrow(() -> new RuntimeException("Objective not found"));
		objective.setContent(objectiveRequestDTO.getContent());
		objective.setIsCompleted(objectiveRequestDTO.getIsCompleted());
		objective.setObjectiveYearMonth(objectiveRequestDTO.getObjectiveYearMonth());
		return objectiveRepository.save(objective);
	}

	public void deleteObjective(Long userId, Long objectiveId) {
		objectiveRepository.deleteById(objectiveId);
	}

	public List<PostResponseDTO> getMyPosts(Long userId) {
		return postRepository.findByUser_UserId(userId).stream()
			.map(post -> new PostResponseDTO(
				post.getPostId(),
				post.getTitle(),
				post.getContent(),
				post.getUser().getUserId(),
				post.getPostType(),
				post.getCreatedAt(),
				post.getUpdatedAt(),
				post.getViewCount(),
				post.getLikeCount()
			))
			.collect(Collectors.toList());
	}

	public List<Map<String, Object>> getMonthlyCompletionStats(Long userId) {
		// 현재 날짜 가져오기
		Timestamp now = new Timestamp(System.currentTimeMillis());
		LocalDate currentDate = now.toLocalDateTime().toLocalDate();

		// 최근 12개월 생성
		List<LocalDate> last12Months = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			last12Months.add(currentDate.minusMonths(i));
		}

		// 목표 가져오기
		List<Objective> objectives = objectiveRepository.findByUser_UserId(userId);

		// 목표를 연-월 기준으로 그룹화
		Map<String, List<Objective>> groupedObjectives = objectives.stream()
			.collect(Collectors.groupingBy(obj -> {
				LocalDate date = obj.getObjectiveYearMonth().toLocalDate();
				return date.getYear() + "-" + String.format("%02d", date.getMonthValue()); // "YYYY-MM" 형식
			}));

		// 결과 데이터 생성
		List<Map<String, Object>> result = new ArrayList<>();
		for (LocalDate month : last12Months) {
			String yearMonth = month.getYear() + "-" + String.format("%02d", month.getMonthValue());

			// 해당 월의 목표 가져오기
			List<Objective> monthObjectives = groupedObjectives.getOrDefault(yearMonth, Collections.emptyList());

			// 완료율 계산
			long total = monthObjectives.size();
			long completed = monthObjectives.stream().filter(Objective::getIsCompleted).count();
			int completionRate = total > 0 ? (int)((completed / (double)total) * 100) : 0;

			// 결과 데이터 추가
			Map<String, Object> monthData = new HashMap<>();
			monthData.put("month", month.getMonthValue());
			monthData.put("amount", completionRate);
			result.add(monthData);
		}

		// 월 순서 뒤집기 (최신순으로)
		Collections.reverse(result);

		return result;
	}
}
