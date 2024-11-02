package com.estsoft.estsoft2ndproject.service;

import org.springframework.stereotype.Service;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostRequestDTO;
import com.estsoft.estsoft2ndproject.exception.UserNotFoundException;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public Post createPost(PostRequestDTO postRequestDTO, Long userId) {
		// 추후에 User 구현 완료되면 로그인한 사용자 Id 가져와서 사용
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return postRepository.save(postRequestDTO.toEntity(user));
	}
}
