package com.estsoft.estsoft2ndproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByPostTypeAndTargetId(String postType, Long targetId);
	List<Post> findByPostType(String postType);
}
