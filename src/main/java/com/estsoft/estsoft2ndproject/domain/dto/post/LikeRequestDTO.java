package com.estsoft.estsoft2ndproject.domain.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequestDTO {
	private Long targetId;
	private Long userId;
}
