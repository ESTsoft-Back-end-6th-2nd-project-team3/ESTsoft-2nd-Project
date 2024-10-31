package com.estsoft.estsoft2ndproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.ActivityScore;

@Repository
public interface ActivityScoreRepository extends JpaRepository<ActivityScore, Long> {
}
