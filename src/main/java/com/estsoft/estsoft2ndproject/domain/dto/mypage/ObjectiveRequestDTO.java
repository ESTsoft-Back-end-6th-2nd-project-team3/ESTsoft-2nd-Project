package com.estsoft.estsoft2ndproject.domain.dto.mypage;

import java.sql.Date;

import lombok.Data;

@Data
public class ObjectiveRequestDTO {
	private Long id;
	private String content;
	private Boolean isCompleted;
	private Date objectiveYearMonth;
}
