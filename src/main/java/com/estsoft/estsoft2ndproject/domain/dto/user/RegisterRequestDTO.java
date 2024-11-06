package com.estsoft.estsoft2ndproject.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
	private String nickname;
	private String profileImageUrl;
	private String selfIntro;
	private String snsLink;
}
