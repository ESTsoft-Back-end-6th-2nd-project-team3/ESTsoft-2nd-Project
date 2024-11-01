package com.estsoft.estsoft2ndproject.domain;

import org.springframework.data.annotation.CreatedDate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
	private String email;

	@Column(name = "nickname", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
	private String nickname;

	@Column(name = "pii", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
	private String pii;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private Timestamp createdAt;

	@Column(name = "updated_at", nullable = false)
	@UpdateTimestamp
	private Timestamp updatedAt;

	@Column(name = "is_active", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean isActive;

	@Column(name = "level", columnDefinition = "VARCHAR(255)", nullable = false)
	private String level;

	@Column(name = "last_login", nullable = false)
	private Timestamp lastLogin;

	@Column(name = "login_count", nullable = false)
	private Integer loginCount;

	@Column(name = "user_agent", columnDefinition = "TEXT", nullable = false)
	private String userAgent;

	@Column(name = "profile_image_url", columnDefinition = "TEXT", nullable = true)
	private String profileImageUrl;

	@Column(name = "activity_score", nullable = false)
	private Integer activityScore;

	@Column(name = "badge_image_data", columnDefinition = "TEXT", nullable = true)
	private String badgeImageData;

	@Column(name = "awarded_title", columnDefinition = "VARCHAR(255)", nullable = true)
	private String awardedTitle;

	@Column(name = "self_intro", columnDefinition = "TEXT", nullable = true)
	private String selfIntro;

	@Column(name = "sns_link", columnDefinition = "TEXT", nullable = true)
	private String snsLink;

	@Builder
	public User(String email, String nickname, String pii, Boolean isActive, String level, Timestamp lastLogin, Integer loginCount, String userAgent, String profileImageUrl, Integer activityScore,
		String badgeImageData, String awardedTitle, String selfIntro, String snsLink) {
		this.email = email;
		this.nickname = nickname;
		this.pii = pii;
		this.isActive = isActive != null ? isActive : true;
		this.level = level;
		this.lastLogin = lastLogin != null ? lastLogin : new Timestamp(System.currentTimeMillis());
		this.loginCount = loginCount != null ? loginCount : 0;
		this.userAgent = userAgent;
		this.profileImageUrl = profileImageUrl;
		this.activityScore = activityScore != null ? activityScore : 0;
		this.badgeImageData = badgeImageData;
		this.awardedTitle = awardedTitle;
		this.selfIntro = selfIntro;
		this.snsLink = snsLink;
	}
}