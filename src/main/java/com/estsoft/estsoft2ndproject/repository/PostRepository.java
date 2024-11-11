package com.estsoft.estsoft2ndproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByIsActiveTrue();

	List<Post> findByPostTypeAndTargetIdAndIsActiveTrue(String postType, Long targetId);

	List<Post> findByPostTypeAndIsActiveTrue(String postType);

	List<Post> findByUser_UserId(Long userId);

	List<Post> findByTitleContainingOrContentContainingAndIsActiveTrue(String titleKeyword, String contentKeyword);

	@Query(
		"SELECT new com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse(p.postId, c.name, p.title, COUNT(cmt), u.nickname, p.viewCount, p.likeCount, p.createdAt, p.isActive) "
			+
			"FROM Post p " +
			"LEFT JOIN Category c ON p.targetId = c.id " +
			"LEFT JOIN Comment cmt ON cmt.post = p " +
			"LEFT JOIN User u ON u.userId = p.user.userId " +
			"GROUP BY p.postId, c.name, u.nickname")
	List<PostListResponse> findPostListWithCategoryAndComments();

	Post findTop1ByPostTypeOrderByCreatedAtDesc(String postType);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTop5PostsByLast7DaysSortedByViewsAndLikes(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo,
		Pageable pageable);

	@Query(
		"SELECT p FROM Post p WHERE p.createdAt >= :today AND p.postType = :postType AND p.targetId = :targetId AND p.isActive = true "
			+
			"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast24Hours(@Param("today") LocalDateTime today, @Param("postType") String postType, @Param("targetId") Long targetId, Pageable pageable);

	@Query(
		"SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.postType = :postType AND p.targetId = :targetId AND p.isActive = true "
			+
			"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast7Days(
		@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, @Param("postType") String postType, @Param("targetId") Long targetId, Pageable pageable);

	Page<Post> findPostsByIsActiveTrueAndPostTypeAndTargetId(String postType, Long TargetId, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :today AND p.postType = :postType AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast24HoursByPostType(@Param("today") LocalDateTime today, @Param("postType") String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.postType = :postType AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast7DaysByPostType(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, @Param("postType") String postType, Pageable pageable);

	Page<Post> findPostsByPostTypeAndIsActiveTrue(String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.isActive = true AND p.postType LIKE %:suffix")
	Page<Post> findPostsByPostTypeSuffixAndIsActiveTrue(
		@Param("suffix") String suffix,
		Pageable pageable
	);
}
