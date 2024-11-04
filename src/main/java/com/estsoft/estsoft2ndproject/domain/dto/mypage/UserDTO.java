package com.estsoft.estsoft2ndproject.domain.dto.mypage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private Long userId;
	private String nickname;
	private String profileImageUrl;
	private String selfIntro;
	private String snsLink;
}
