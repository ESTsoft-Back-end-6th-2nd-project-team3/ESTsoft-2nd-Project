package com.estsoft.estsoft2ndproject.service;

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

import java.util.List;
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

	public void updateMyInfo(Long userId, UserInfoResponseDTO userInfo) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.updateUser(userInfo.getNickname(), null, null, null, null, null, userInfo.getProfileImageUrl(),
			userInfo.getActivityScore(), null, userInfo.getAwardedTitle(), userInfo.getSelfIntro(), userInfo.getSnsLink());
		userRepository.save(user);
	}

	public List<Objective> getObjectives(Long userId) {
		return objectiveRepository.findByUser_UserId(userId);
	}

	public void saveOrUpdateObjectives(Long userId, List<ObjectiveRequestDTO> objectiveRequestDTOs) {
		List<Objective> existingObjectives = objectiveRepository.findByUser_UserId(userId);

		List<Long> requestedIds = objectiveRequestDTOs.stream()
			.map(ObjectiveRequestDTO::getId)
			.toList();

		List<Objective> objectivesToDelete = existingObjectives.stream()
			.filter(obj -> !requestedIds.contains(obj.getId()))
			.collect(Collectors.toList());

		objectiveRepository.deleteAll(objectivesToDelete);

		for (ObjectiveRequestDTO dto : objectiveRequestDTOs) {
			if (dto.getId() != null) {
				Objective objective = objectiveRepository.findById(dto.getId())
					.orElseThrow(() -> new RuntimeException("Objective not found"));
				objective.setContent(dto.getContent());
				objective.setIsCompleted(dto.getIsCompleted());
				objective.setObjectiveYearMonth(dto.getObjectiveYearMonth());
				objectiveRepository.save(objective);
			} else {
				Objective newObjective = new Objective();
				newObjective.setUser(userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found")));
				newObjective.setContent(dto.getContent());
				newObjective.setObjectiveYearMonth(dto.getObjectiveYearMonth());
				newObjective.setIsCompleted(dto.getIsCompleted());
				objectiveRepository.save(newObjective);
			}
		}
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
				post.getPostType(),
				post.getViewCount(),
				post.getLikeCount(),
				post.getCreatedAt(),
				post.getUpdatedAt()
			))
			.collect(Collectors.toList());
	}
}
