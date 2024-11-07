package com.estsoft.estsoft2ndproject.domain.dto.user;

import java.sql.Timestamp;

import com.estsoft.estsoft2ndproject.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	private String email;
	private String nickname;
	private String pii;
	private Boolean isActive;
	private String level;
	private Timestamp lastLogin;
	private Integer loginCount;
	private String userAgent;
	private String profileImageUrl;
	private Integer activityScore;
	private String badgeImageData;
	private String awardedTitle;
	private String selfIntro;
	private String snsLink;

	public User toEntity() {
		return User.builder()
			.email(this.email)
			.nickname(this.nickname)
			.pii(this.pii)
			.isActive(this.isActive)
			.level(this.level)
			.lastLogin(this.lastLogin)
			.loginCount(this.loginCount)
			.userAgent(this.userAgent)
			.profileImageUrl(this.profileImageUrl)
			.activityScore(this.activityScore)
			.badgeImageData(this.badgeImageData)
			.awardedTitle(this.awardedTitle)
			.selfIntro(this.selfIntro)
			.snsLink(this.snsLink)
			.build();
	}
}
