package com.estsoft.estsoft2ndproject.repository;

import com.estsoft.estsoft2ndproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByUser_UserId(Long userId);
}
