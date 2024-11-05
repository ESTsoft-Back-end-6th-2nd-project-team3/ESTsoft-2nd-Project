package com.estsoft.estsoft2ndproject.domain.dto.post;

import java.sql.Timestamp;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDTO {
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

	public PostResponseDTO(Post post) {
		User user = post.getUser();

		this.postId = post.getPostId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.userId = user.getUserId();
		this.createdAt = post.getCreatedAt();
		this.updatedAt = post.getUpdatedAt();
		this.postType = post.getPostType();
		this.targetId = post.getTargetId();
		this.isActive = post.getIsActive();
		this.viewCount = post.getViewCount();
		this.likeCount = post.getLikeCount();
	}
}
