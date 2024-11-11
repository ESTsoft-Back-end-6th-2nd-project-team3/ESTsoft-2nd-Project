package com.estsoft.estsoft2ndproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.Comment;
import com.estsoft.estsoft2ndproject.domain.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPostAndIsActive(Post postId, Boolean isActive);

	Integer countByPostAndIsActive(Post post, Boolean isActive);

	@Query("SELECT COUNT(c) FROM Comment c WHERE c.post.postId = :postId")
	int countCommentsByPostId(@Param("postId") Long postId);
}
