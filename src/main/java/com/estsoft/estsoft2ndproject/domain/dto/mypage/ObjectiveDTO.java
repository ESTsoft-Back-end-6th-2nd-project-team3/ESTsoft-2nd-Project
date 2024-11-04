package com.estsoft.estsoft2ndproject.domain.dto.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ObjectiveDTO {
	private Long objectiveId;
	private String content;
	private Long userId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Date objectiveYearMonth;
	private Boolean isCompleted;
}
