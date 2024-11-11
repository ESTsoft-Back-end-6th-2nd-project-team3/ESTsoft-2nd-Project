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
public class CommentResponseDTO {
	private Long commentId;
	private String content;
	private Long postId;
	private Long userId;
	private Long parentCommentId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp updatedAt;
	private Integer likeCount;
	private String nickname;
	private String profileUrl;

	public CommentResponseDTO(Comment comment) {
		this.commentId = comment.getCommentId();
		this.content = comment.getContent();
		this.postId = comment.getPost().getPostId();
		this.userId = comment.getUser().getUserId();
		if (comment.getParentComment() != null) {
			this.parentCommentId = comment.getParentComment().getCommentId();
		}
		this.updatedAt = comment.getUpdatedAt();
		this.likeCount = comment.getLikeCount();
	}

	public CommentResponseDTO(Long commentId, String content, Long postId, Long userId, Long parentCommentId,
		Timestamp updatedAt, Integer likeCount) {
		this.commentId = commentId;
		this.content = content;
		this.postId = postId;
		this.userId = userId;
		this.parentCommentId = parentCommentId;
		this.updatedAt = updatedAt;
		this.likeCount = likeCount;
	}
}
