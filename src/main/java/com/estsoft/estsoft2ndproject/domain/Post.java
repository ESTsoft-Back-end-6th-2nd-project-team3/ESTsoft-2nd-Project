package com.estsoft.estsoft2ndproject.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@Table(name = "POST")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Column(name = "title", columnDefinition = "VARCHAR(255)", nullable = false)
	private String title;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private Timestamp createdAt;

	@Column(name = "updated_at", nullable = false)
	@UpdateTimestamp
	private Timestamp updatedAt;

	@Column(name = "post_type", columnDefinition = "VARCHAR(255)", nullable = false)
	private String postType;

	@Column(name = "target_id", nullable = true)
	private Long targetId;

	@Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean isActive;

	@Column(name = "view_count", nullable = false)
	private Integer viewCount;

	@Column(name = "like_count", nullable = false)
	private Integer likeCount;

	@Builder
	public Post(String content, String title, User user, String postType, Long targetId, Boolean isActive, Integer viewCount, Integer likeCount) {
		this.content = content;
		this.title = title;
		this.user = user;
		this.postType = postType;
		this.targetId = targetId;
		this.isActive = isActive != null ? isActive : true;
		this.viewCount = viewCount != null ? viewCount : 0;
		this.likeCount = likeCount != null ? likeCount : 0;
	}
}