package com.estsoft.estsoft2ndproject.service;

import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveDTO;
import com.estsoft.estsoft2ndproject.repository.UserRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ObjectiveRepository objectiveRepository;

	// 내 정보 조회
	public UserDTO getMyInfoDTO(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		if (user == null) {
			return null; // 유저가 존재하지 않을 경우 null 반환
		}

		// User 엔티티를 UserDTO로 변환
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(user.getUserId());
		userDTO.setNickname(user.getNickname());
		userDTO.setProfileImageUrl(user.getProfileImageUrl());
		userDTO.setSelfIntro(user.getSelfIntro());
		userDTO.setSnsLink(user.getSnsLink());

		return userDTO;
	}

	// 내 정보 수정
	public UserDTO updateMyInfoDTO(Long userId, UserDTO updatedUser) {
		return userRepository.findById(userId).map(user -> {
			// 유저 정보 업데이트
			user.setNickname(updatedUser.getNickname());
			user.setProfileImageUrl(updatedUser.getProfileImageUrl());
			user.setSelfIntro(updatedUser.getSelfIntro());
			user.setSnsLink(updatedUser.getSnsLink());
			userRepository.save(user); // 업데이트된 유저 정보 저장

			// 수정된 User 엔티티를 UserDTO로 변환
			UserDTO userDTO = new UserDTO();
			userDTO.setUserId(user.getUserId());
			userDTO.setNickname(user.getNickname());
			userDTO.setProfileImageUrl(user.getProfileImageUrl());
			userDTO.setSelfIntro(user.getSelfIntro());
			userDTO.setSnsLink(user.getSnsLink());

			return userDTO;
		}).orElse(null); // 유저가 존재하지 않을 경우 null 반환
	}

	// 내가 작성한 게시글 조회
	public List<PostDTO> getMyPostsDTO(Long userId) {
		List<Post> posts = postRepository.findByUser_UserId(userId);

		// Post 엔티티를 PostDTO로 변환
		return posts.stream().map(post -> {
			PostDTO postDTO = new PostDTO();
			postDTO.setPostId(post.getPostId());
			postDTO.setTitle(post.getTitle());
			postDTO.setContent(post.getContent());
			return postDTO;
		}).collect(Collectors.toList());
	}

	// 목표 조회
	public List<ObjectiveDTO> getObjectiveDTO(Long userId) {
		List<Objective> objectives = objectiveRepository.findByUser_UserId(userId);

		// Objective 엔티티를 ObjectiveDTO로 변환
		return objectives.stream().map(objective -> {
			ObjectiveDTO objectiveDTO = new ObjectiveDTO();
			objectiveDTO.setObjectiveId(objective.getId());
			objectiveDTO.setContent(objective.getContent());
			objectiveDTO.setIsCompleted(objective.getIsCompleted());
			return objectiveDTO;
		}).collect(Collectors.toList());
	}
}
