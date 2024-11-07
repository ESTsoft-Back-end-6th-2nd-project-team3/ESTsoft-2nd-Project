package com.estsoft.estsoft2ndproject.controller.api;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.UserInfoResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.ObjectiveRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.mypage.PostResponseDTO;
import com.estsoft.estsoft2ndproject.service.MyPageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class MyPageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MyPageService myPageService;

	@Test
	public void testGetMyInfo() throws Exception {
		UserInfoResponseDTO mockUser = new UserInfoResponseDTO(1L, "user@example.com", "nickname", "profile.png", "title", "intro", "snsLink", 100);

		Mockito.when(myPageService.getMyInfo(anyLong())).thenReturn(mockUser);

		mockMvc.perform(get("/mypage/1/userinfo"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").value(1))
			.andExpect(jsonPath("$.email").value("user@example.com"));
	}

	@Test
	public void testUpdateMyInfo() throws Exception {
		UserInfoResponseDTO updateInfo = new UserInfoResponseDTO(1L, "updated@example.com", "newNick", "newProfile.png", "newTitle", "newIntro", "newLink", 200);

		mockMvc.perform(put("/mypage/1/userinfo")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"userId\":1,\"email\":\"updated@example.com\",\"nickname\":\"newNick\",\"profileImageUrl\":\"newProfile.png\",\"awardedTitle\":\"newTitle\",\"selfIntro\":\"newIntro\",\"snsLink\":\"newLink\",\"activityScore\":200}"))
			.andExpect(status().isOk());
	}

	@Test
	public void testGetObjectives() throws Exception {
		Objective mockObjective = new Objective();
		mockObjective.setId(1L);
		mockObjective.setContent("My Objective");
		mockObjective.setObjectiveYearMonth(new Date(System.currentTimeMillis()));
		mockObjective.setIsCompleted(false);

		Mockito.when(myPageService.getObjectives(anyLong())).thenReturn(Collections.singletonList(mockObjective));

		mockMvc.perform(get("/mypage/1/objectives"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].content").value("My Objective"));
	}

	@Test
	public void testCreateObjective() throws Exception {
		ObjectiveRequestDTO objectiveRequest = new ObjectiveRequestDTO();
		objectiveRequest.setContent("New Objective");
		objectiveRequest.setIsCompleted(false);
		objectiveRequest.setObjectiveYearMonth(new Date(System.currentTimeMillis()));

		Objective createdObjective = new Objective();
		createdObjective.setId(1L);
		createdObjective.setContent("New Objective");
		createdObjective.setIsCompleted(false);
		createdObjective.setObjectiveYearMonth(new Date(System.currentTimeMillis()));

		Mockito.when(myPageService.createObjective(anyLong(), any(ObjectiveRequestDTO.class))).thenReturn(createdObjective);

		mockMvc.perform(post("/mypage/1/objective")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"content\":\"New Objective\",\"isCompleted\":false,\"objectiveYearMonth\":\"2024-01-01\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.content").value("New Objective"));
	}

	@Test
	public void testUpdateObjective() throws Exception {
		Objective updatedObjective = new Objective();
		updatedObjective.setId(1L);
		updatedObjective.setContent("Updated Objective");
		updatedObjective.setIsCompleted(true);
		updatedObjective.setObjectiveYearMonth(new Date(System.currentTimeMillis()));

		Mockito.when(myPageService.updateObjective(anyLong(), anyLong(), any(ObjectiveRequestDTO.class))).thenReturn(updatedObjective);

		mockMvc.perform(put("/mypage/1/objective/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"content\":\"Updated Objective\",\"isCompleted\":true,\"objectiveYearMonth\":\"2024-01-01\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.content").value("Updated Objective"));
	}

	@Test
	public void testDeleteObjective() throws Exception {
		mockMvc.perform(delete("/mypage/1/objective/1"))
			.andExpect(status().isOk());
	}

	@Test
	public void testGetMyPosts() throws Exception {
		PostResponseDTO mockPost = new PostResponseDTO(
			1L,
			"Post Title",
			"Post Content",
			"General",
			100,
			50,
			new Timestamp(System.currentTimeMillis()),
			new Timestamp(System.currentTimeMillis())
		);

		Mockito.when(myPageService.getMyPosts(anyLong())).thenReturn(Collections.singletonList(mockPost));

		mockMvc.perform(get("/mypage/1/posts"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].postId").value(1))
			.andExpect(jsonPath("$[0].title").value("Post Title"))
			.andExpect(jsonPath("$[0].content").value("Post Content"))
			.andExpect(jsonPath("$[0].postType").value("General"))
			.andExpect(jsonPath("$[0].viewCount").value(100))
			.andExpect(jsonPath("$[0].likeCount").value(50));
	}
}
