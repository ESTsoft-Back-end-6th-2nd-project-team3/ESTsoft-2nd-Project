package com.estsoft.estsoft2ndproject.domain.dto.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
	private Long postId;
	private String title;
	private String content;
	private Long userId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String postType;
	private Long targetId;
	private Boolean isActive;
	private Integer viewCount;
	private Integer likeCount;
}
