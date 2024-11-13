package com.estsoft.estsoft2ndproject.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.Likes;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
	Likes findByTargetIdAndUser_UserId(Long targetId, Long userId);

	@Query("SELECT l.targetId, COUNT(l.likeId) as likeCount " +
		"FROM Likes l " +
		"WHERE l.createdAt >= :startOfDay " +
		"AND l.createdAt <= :endOfDay " +
		"GROUP BY l.targetId " +
		"ORDER BY likeCount DESC")
	List<Object[]> findTopLikedPostsToday(@Param("startOfDay") Timestamp startOfDay,
		@Param("endOfDay") Timestamp endOfDay);
}
