package com.estsoft.estsoft2ndproject.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByIsActiveTrue();

	List<Post> findByPostTypeAndTargetIdAndIsActiveTrue(String postType, Long targetId);

	List<Post> findByPostTypeAndIsActiveTrue(String postType);

	List<Post> findByUser_UserId(Long userId);

	List<Post> findByTitleContainingOrContentContainingAndIsActiveTrue(String titleKeyword, String contentKeyword);

	Page<Post> findPostsByTitleContainingOrContentContainingAndIsActiveTrue(String titleKeyword, String contentKeyword, Pageable pageable);

	Page<Post> findPostsByUserAndIsActiveTrue(User user, Pageable pageable);

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

	@Query("SELECT p FROM Post p WHERE p.isActive = true ORDER BY p.createdAt DESC")
	List<Post> findAllByIsActiveTrueOrderByCreatedAtDesc();


	Post findTop1ByPostTypeOrderByCreatedAtDesc(String postType);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTop5PostsByLast7DaysSortedByViewsAndLikes(@Param("sevenDaysAgo") Timestamp sevenDaysAgo,
		Pageable pageable);

	@Query(
		"SELECT p FROM Post p WHERE p.createdAt >= :today AND p.postType = :postType AND p.targetId = :targetId AND p.isActive = true "
			+
			"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast24Hours(@Param("today") Timestamp today,
		@Param("postType")String postType, @Param("targetId")Long targetId, Pageable pageable);

	@Query(
		"SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.postType = :postType AND p.targetId = :targetId AND p.isActive = true "
			+
			"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast7Days(
		@Param("sevenDaysAgo") Timestamp sevenDaysAgo, @Param("postType")String postType, @Param("targetId")Long targetId,
		Pageable pageable);

	Page<Post> findPostsByIsActiveTrueAndPostTypeAndTargetId(String postType, Long TargetId, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :today AND p.postType = :postType AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast24HoursByPostType(@Param("today") Timestamp today, @Param("postType") String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.postType = :postType AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast7DaysByPostType(@Param("sevenDaysAgo") Timestamp sevenDaysAgo, @Param("postType") String postType, Pageable pageable);

	Page<Post> findPostsByPostTypeAndIsActiveTrue(String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.isActive = true AND p.postType LIKE %:suffix")
	Page<Post> findPostsByPostTypeSuffixAndIsActiveTrue(
		@Param("suffix") String suffix,
		Pageable pageable
	);

	Page<Post> findByTitleContainingAndIsActiveTrueAndPostTypeAndTargetId(String title, String postType, Long targetId,
		Pageable pageable);

	Page<Post> findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeAndTargetId(String titleKeyword,
		String contentKeyword, String postType, Long targetId, Pageable pageable);

	Page<Post> findByTitleContainingAndIsActiveTrueAndPostType(String title, String postType, Pageable pageable);

	Page<Post> findByTitleContainingOrContentContainingAndIsActiveTrueAndPostType(String titleKeyword,
		String contentKeyword, String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.isActive = true AND p.title LIKE :title AND p.postType LIKE :suffix")
	Page<Post> findByTitleContainingAndIsActiveTrueAndPostTypeSuffix(
		@Param("title") String title,
		@Param("suffix") String suffix,
		Pageable pageable
	);

	@Query("SELECT p FROM Post p WHERE p.isActive = true AND (p.title LIKE :titleKeyword OR p.content LIKE :contentKeyword) AND p.postType LIKE :suffix")
	Page<Post> findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeSuffix(
		@Param("titleKeyword") String titleKeyword,
		@Param("contentKeyword") String contentKeyword,
		@Param("suffix") String suffix,
		Pageable pageable
	);

}
