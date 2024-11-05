package com.estsoft.estsoft2ndproject.controller.api;

import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveDTO;
import com.estsoft.estsoft2ndproject.repository.UserRepository;
import com.estsoft.estsoft2ndproject.service.MyPageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import java.util.Optional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyPageController.class)
public class MyPageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MyPageService myPageService;

	@MockBean
	private UserRepository userRepository; // UserRepository를 MockBean으로 추가

	@Test
	@WithMockUser(username = "testUser", roles = {"USER"})
	public void getMyInfo_ShouldReturnUserDTO() throws Exception {
		Long userId = 1L;
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userId);
		userDTO.setNickname("testNickname");
		userDTO.setProfileImageUrl("http://example.com/profile.jpg");
		userDTO.setSelfIntro("This is a test self introduction");
		userDTO.setSnsLink("http://example.com/sns");

		// Mocking the service call
		when(myPageService.getMyInfoDTO(userId)).thenReturn(userDTO);

		// Perform the GET request and check the response
		mockMvc.perform(get("/mypage/userinfo")
				.sessionAttr("user_id", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(userId))
			.andExpect(jsonPath("$.nickname").value("testNickname"))
			.andExpect(jsonPath("$.profileImageUrl").value("http://example.com/profile.jpg"))
			.andExpect(jsonPath("$.selfIntro").value("This is a test self introduction"))
			.andExpect(jsonPath("$.snsLink").value("http://example.com/sns"));
	}

	@Test
	@WithMockUser(username = "testUser", roles = {"USER"})
	public void updateMyInfo_ShouldReturnUpdatedUserDTO() throws Exception {
		Long userId = 1L;
		UserDTO updatedUser = new UserDTO();
		updatedUser.setUserId(userId);
		updatedUser.setNickname("updatedNickname");
		updatedUser.setProfileImageUrl("http://example.com/updated.jpg");
		updatedUser.setSelfIntro("Updated self introduction");
		updatedUser.setSnsLink("http://example.com/updated-sns");

		// Mocking the service call to return the updated user
		when(myPageService.updateMyInfoDTO(eq(userId), any(UserDTO.class))).thenReturn(updatedUser);

		// Perform the PUT request and check the response
		mockMvc.perform(put("/mypage/userinfo")
				.sessionAttr("user_id", userId)
				.contentType("application/json")
				.content("{\"userId\":" + userId + ",\"nickname\":\"updatedNickname\",\"profileImageUrl\":\"http://example.com/updated.jpg\",\"selfIntro\":\"Updated self introduction\",\"snsLink\":\"http://example.com/updated-sns\"}")
				.with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF token
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(userId))
			.andExpect(jsonPath("$.nickname").value("updatedNickname"))
			.andExpect(jsonPath("$.profileImageUrl").value("http://example.com/updated.jpg"))
			.andExpect(jsonPath("$.selfIntro").value("Updated self introduction"))
			.andExpect(jsonPath("$.snsLink").value("http://example.com/updated-sns"));
	}



	@Test
	@WithMockUser(username = "testUser", roles = {"USER"})
	public void getMyPosts_ShouldReturnListOfPostDTO() throws Exception {
		Long userId = 1L;
		PostDTO post1 = new PostDTO();
		post1.setPostId(1L);
		post1.setTitle("First Post");
		post1.setContent("Content of the first post");

		PostDTO post2 = new PostDTO();
		post2.setPostId(2L);
		post2.setTitle("Second Post");
		post2.setContent("Content of the second post");

		List<PostDTO> posts = Arrays.asList(post1, post2);

		// Mocking the service call
		when(myPageService.getMyPostsDTO(userId)).thenReturn(posts);

		// Perform the POST request and check the response
		mockMvc.perform(post("/mypage/posts")
				.sessionAttr("user_id", userId)
				.with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF token
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].postId").value(1L))
			.andExpect(jsonPath("$[0].title").value("First Post"))
			.andExpect(jsonPath("$[0].content").value("Content of the first post"))
			.andExpect(jsonPath("$[1].postId").value(2L))
			.andExpect(jsonPath("$[1].title").value("Second Post"))
			.andExpect(jsonPath("$[1].content").value("Content of the second post"));
	}

	@Test
	@WithMockUser(username = "testUser", roles = {"USER"})
	public void getObjective_ShouldReturnListOfObjectiveDTO() throws Exception {
		Long userId = 1L;
		ObjectiveDTO objective1 = new ObjectiveDTO();
		objective1.setObjectiveId(1L);
		objective1.setContent("First objective");
		objective1.setIsCompleted(false);

		ObjectiveDTO objective2 = new ObjectiveDTO();
		objective2.setObjectiveId(2L);
		objective2.setContent("Second objective");
		objective2.setIsCompleted(true);

		List<ObjectiveDTO> objectives = Arrays.asList(objective1, objective2);

		// Mocking the service call
		when(myPageService.getObjectiveDTO(userId)).thenReturn(objectives);

		// Perform the GET request and check the response
		mockMvc.perform(get("/mypage/objective")
				.sessionAttr("user_id", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].objectiveId").value(1L))
			.andExpect(jsonPath("$[0].content").value("First objective"))
			.andExpect(jsonPath("$[0].isCompleted").value(false))
			.andExpect(jsonPath("$[1].objectiveId").value(2L))
			.andExpect(jsonPath("$[1].content").value("Second objective"))
			.andExpect(jsonPath("$[1].isCompleted").value(true));
	}
}
