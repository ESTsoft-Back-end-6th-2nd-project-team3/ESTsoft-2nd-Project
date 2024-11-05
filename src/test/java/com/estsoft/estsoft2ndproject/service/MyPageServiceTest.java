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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyPageServiceTest {

	@InjectMocks
	private MyPageService myPageService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PostRepository postRepository;

	@Mock
	private ObjectiveRepository objectiveRepository;

	@Test
	public void getMyInfoDTO_ShouldReturnUserDTO() {
		Long userId = 1L;
		User user = new User();
		user.setUserId(userId);
		user.setNickname("Test User");
		user.setProfileImageUrl("http://example.com/profile.jpg");
		user.setSelfIntro("This is a test self introduction");
		user.setSnsLink("http://example.com/sns");

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		UserDTO result = myPageService.getMyInfoDTO(userId);

		assertNotNull(result);
		assertEquals(userId, result.getUserId());
		assertEquals("Test User", result.getNickname());
		assertEquals("http://example.com/profile.jpg", result.getProfileImageUrl());
		assertEquals("This is a test self introduction", result.getSelfIntro());
		assertEquals("http://example.com/sns", result.getSnsLink());
	}

	@Test
	public void updateMyInfoDTO_ShouldReturnUpdatedUserDTO() {
		Long userId = 1L;
		User user = new User();
		user.setUserId(userId);

		UserDTO updatedUser = new UserDTO();
		updatedUser.setUserId(userId);
		updatedUser.setNickname("Updated User");
		updatedUser.setProfileImageUrl("http://example.com/updated.jpg");
		updatedUser.setSelfIntro("Updated self introduction");
		updatedUser.setSnsLink("http://example.com/updated-sns");

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);

		UserDTO result = myPageService.updateMyInfoDTO(userId, updatedUser);

		assertNotNull(result);
		assertEquals("Updated User", result.getNickname());
		assertEquals("http://example.com/updated.jpg", result.getProfileImageUrl());
		assertEquals("Updated self introduction", result.getSelfIntro());
		assertEquals("http://example.com/updated-sns", result.getSnsLink());
	}

	@Test
	public void getMyPostsDTO_ShouldReturnListOfPostDTO() {
		Long userId = 1L;
		Post post1 = new Post();
		post1.setPostId(1L);
		post1.setTitle("First Post");
		post1.setContent("Content of the first post");

		Post post2 = new Post();
		post2.setPostId(2L);
		post2.setTitle("Second Post");
		post2.setContent("Content of the second post");

		List<Post> posts = Arrays.asList(post1, post2);
		when(postRepository.findByUser_UserId(userId)).thenReturn(posts);

		List<PostDTO> result = myPageService.getMyPostsDTO(userId);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("First Post", result.get(0).getTitle());
		assertEquals("Second Post", result.get(1).getTitle());
	}

	@Test
	public void getObjectiveDTO_ShouldReturnListOfObjectiveDTO() {
		Long userId = 1L;
		Objective objective1 = new Objective();
		objective1.setId(1L);
		objective1.setContent("First Objective");
		objective1.setIsCompleted(false);

		Objective objective2 = new Objective();
		objective2.setId(2L);
		objective2.setContent("Second Objective");
		objective2.setIsCompleted(true);

		List<Objective> objectives = Arrays.asList(objective1, objective2);
		when(objectiveRepository.findByUser_UserId(userId)).thenReturn(objectives);

		List<ObjectiveDTO> result = myPageService.getObjectiveDTO(userId);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("First Objective", result.get(0).getContent());
		assertTrue(result.get(1).getIsCompleted());
	}
}
