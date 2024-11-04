package com.estsoft.estsoft2ndproject.repository;

import com.estsoft.estsoft2ndproject.domain.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
	List<Objective> findByUser_UserId(Long userId);
}
