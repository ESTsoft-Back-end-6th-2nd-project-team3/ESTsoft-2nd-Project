package com.estsoft.estsoft2ndproject.domain.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDTO {
	private String nickname;
	private String profileImageUrl;
	private String selfIntro;
	private String snsLink;
}
