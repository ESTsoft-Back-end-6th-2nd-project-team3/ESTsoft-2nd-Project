package com.estsoft.estsoft2ndproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "LIKES")
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
}