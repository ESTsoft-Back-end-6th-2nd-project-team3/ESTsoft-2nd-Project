package com.estsoft.estsoft2ndproject.domain.dto.user;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
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
}
