package com.estsoft.estsoft2ndproject.domain.dto.comment;

import java.sql.Timestamp;

import com.estsoft.estsoft2ndproject.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponseDTO {
	private Long commentId;
	private String content;
	private Long postId;
	private Long userId;
	private String nickname;
	private String profileImageUrl;
	private Long parentCommentId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Integer likeCount;

	public CommentListResponseDTO(Comment comment) {
		this.commentId = comment.getCommentId();
		this.content = comment.getContent();
		this.postId = comment.getPost().getPostId();
		this.userId = comment.getUser().getUserId();
		this.nickname = comment.getUser().getNickname();
		this.profileImageUrl = comment.getUser().getProfileImageUrl();
		if (comment.getParentComment() != null) {
			this.parentCommentId = comment.getParentComment().getCommentId();
		}
		this.createdAt = comment.getCreatedAt();
		this.updatedAt = comment.getUpdatedAt();
		this.likeCount = comment.getLikeCount();
	}
}
