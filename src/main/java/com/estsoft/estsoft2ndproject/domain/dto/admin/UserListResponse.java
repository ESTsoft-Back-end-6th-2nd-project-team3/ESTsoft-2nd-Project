package com.estsoft.estsoft2ndproject.domain.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserListResponse {
	private Long userId;
	private String email;
	private String nickname;
	private String level;
	private Integer activityScore;
	private Boolean isActive;
}
