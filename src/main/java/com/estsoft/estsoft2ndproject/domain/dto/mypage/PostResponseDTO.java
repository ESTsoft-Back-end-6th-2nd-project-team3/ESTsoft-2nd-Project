package com.estsoft.estsoft2ndproject.domain.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
	private Long postId;
	private String title;
	private String content;
	private Long userId;
	private String postType;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Integer viewCount;
	private Integer likeCount;
}
