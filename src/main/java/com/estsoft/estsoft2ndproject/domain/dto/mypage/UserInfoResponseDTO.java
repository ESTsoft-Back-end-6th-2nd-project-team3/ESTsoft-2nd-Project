package com.estsoft.estsoft2ndproject.domain.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDTO {
	private Long userId;
	private String email;
	private String nickname;
	private String profileImageUrl;
	private String awardedTitle;
	private String selfIntro;
	private String snsLink;
	private int activityScore;
}
