package com.estsoft.estsoft2ndproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByNickname(String nickname);
}
