package com.estsoft.estsoft2ndproject.domain.dto.admin;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostListResponse {
	private Long postId;
	private String categoryName;
	private String title;
	private Long commentCount;
	private String nickname;
	private Integer viewCount;
	private Integer likeCount;
	private Timestamp createdAt;
	private Boolean isActive;
}
