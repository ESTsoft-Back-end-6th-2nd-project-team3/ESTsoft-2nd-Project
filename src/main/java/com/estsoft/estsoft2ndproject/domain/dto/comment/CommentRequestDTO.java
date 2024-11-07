package com.estsoft.estsoft2ndproject.domain.dto.comment;

import com.estsoft.estsoft2ndproject.domain.Comment;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
	private String content;

	public Comment toEntity(Post post, User user) {
		return Comment.builder()
			.content(this.content)
			.post(post)
			.user(user)
			.build();
	}

	public Comment toEntity(Post post, User user, Comment parentComment) {
		return Comment.builder()
			.content(this.content)
			.post(post)
			.user(user)
			.parentComment(parentComment)
			.build();
	}
}
