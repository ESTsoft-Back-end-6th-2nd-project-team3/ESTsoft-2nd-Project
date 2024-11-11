package com.estsoft.estsoft2ndproject.domain.dto.post;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {
	private String title;
	private String content;
	private String postType;
	private Long targetId;
	private Boolean isActive;
	@Setter
	private Integer viewCount;
	private Integer likeCount;

	public Post toEntity(User user) {
		return Post.builder()
			.title(this.title)
			.content(this.content)
			.user(user)
			.postType(this.postType)
			.targetId(this.targetId)
			.isActive(this.isActive)
			.viewCount(this.viewCount)
			.likeCount(this.likeCount)
			.build();
	}
}
