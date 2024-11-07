package com.estsoft.estsoft2ndproject.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentResponseDTO;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long commentId;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "parent_comment_id", nullable = true)
	private Comment parentComment;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private Timestamp createdAt;

	@Column(name = "updated_at", nullable = false)
	@UpdateTimestamp
	private Timestamp updatedAt;

	@Column(name = "is_active", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean isActive;

	@Column(name = "like_count", nullable = false)
	private Integer likeCount;

	@Builder
	public Comment(String content, Post post, User user, Comment parentComment, Boolean isActive, Integer likeCount) {
		this.content = content;
		this.post = post;
		this.user = user;
		this.parentComment = parentComment;
		this.isActive = isActive != null ? isActive : true;
		this.likeCount = likeCount != null ? likeCount : 0;
	}

	public CommentResponseDTO convert() {
		return new CommentResponseDTO(this.commentId, this.content, this.post.getPostId(), this.user.getUserId(), this.parentComment != null ? this.parentComment.getCommentId() : null,
			this.updatedAt, this.likeCount);
	}
}
