package com.estsoft.estsoft2ndproject.domain;

import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ACTIVITY_SCORE")
@Getter
@Setter
@NoArgsConstructor
public class ActivityScore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "score_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "score_fluctuations", nullable = false)
	private int scoreFluctuations;

	@Column(name = "fluctuation_at", nullable = false)
	@CreatedDate
	private Timestamp fluctuationAt;

	@Column(name = "fluctuation_reason", columnDefinition = "VARCHAR(255)", nullable = false)
	private String fluctuationReason;
}