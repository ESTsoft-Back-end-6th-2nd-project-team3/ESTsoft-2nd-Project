package com.estsoft.estsoft2ndproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByIsActiveTrue();

	List<Post> findByPostTypeAndTargetIdAndIsActiveTrue(String postType, Long targetId);

	List<Post> findByPostTypeAndIsActiveTrue(String postType);

	List<Post> findByUser_UserId(Long userId);

	List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

	@Query(
		"SELECT new com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse(p.postId, c.name, p.title, COUNT(cmt), u.nickname, p.viewCount, p.likeCount, p.createdAt, p.isActive) "
			+
			"FROM Post p " +
			"LEFT JOIN Category c ON p.targetId = c.id " +
			"LEFT JOIN Comment cmt ON cmt.post = p " +
			"LEFT JOIN User u ON u.userId = p.user.userId " +
			"GROUP BY p.postId, c.name, u.nickname")
	List<PostListResponse> findPostListWithCategoryAndComments();

	// 공지사항 데이터
	@Query("SELECT p FROM Post p WHERE p.postType = 'announcement' AND p.isActive = true")
	List<Post> findAnnouncements();

	List<Post> findByUserUserIdOrderByCreatedAtDesc(Long userId);

}
