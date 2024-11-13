package com.estsoft.estsoft2ndproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByPii(String pii);

	User findByUserId(Long userId);

	boolean existsByNickname(String nickname);
}
