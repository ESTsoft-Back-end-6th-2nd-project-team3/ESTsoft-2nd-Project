package com.estsoft.estsoft2ndproject.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.repository.ObjectiveRepository;

@Service
public class ObjectiveService {

	private final ObjectiveRepository objectiveRepository;

	public ObjectiveService(ObjectiveRepository objectiveRepository) {
		this.objectiveRepository = objectiveRepository;
	}

	public List<Objective> getObjectivesForUserAndMonth(Long userId, LocalDate month) {
		return objectiveRepository.findByUser_UserIdAndObjectiveYearMonth(userId, Date.valueOf(month));
	}
}