package com.estsoft.estsoft2ndproject.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.ActivityScore;

@Repository
public interface ActivityScoreRepository extends JpaRepository<ActivityScore, Long> {
	@Query("SELECT a.user.userId, a.user.nickname, a.user.level, SUM(a.scoreFluctuations) as totalScore " +
		"FROM ActivityScore a " +
		"WHERE a.fluctuationAt BETWEEN :startDate AND :endDate " +
		"GROUP BY a.user.userId, a.user.nickname, a.user.level " +
		"ORDER BY totalScore DESC")
	List<Object[]> findMonthlyTopUsers(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

}
