package com.estsoft.estsoft2ndproject.domain.dto.activityScore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequestDTO {
	private int score;
	private String reason;
}
