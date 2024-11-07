package com.estsoft.estsoft2ndproject.domain;

import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "LIKES")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_id", nullable = false)
	private Long likeId;

	@Column(name = "like_type", columnDefinition = "VARCHAR(255)", nullable = false)
	private String likeType;

	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private Timestamp createdAt;

	public Likes(String likeType, Long targetId, User user) {
		this.likeType = likeType;
		this.targetId = targetId;
		this.user = user;
	}
}