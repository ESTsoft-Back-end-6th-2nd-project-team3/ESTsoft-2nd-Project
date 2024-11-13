package com.estsoft.estsoft2ndproject.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
	List<Post> findAllByIsActiveTrue();

	List<Post> findByPostTypeAndTargetIdAndIsActiveTrue(String postType, Long targetId);

	List<Post> findByPostTypeAndIsActiveTrueOrderByCreatedAtDesc(String postType);

	List<Post> findByUser_UserId(Long userId);

	List<Post> findByTitleContainingOrContentContainingAndIsActiveTrueOrderByCreatedAtDesc(String titleKeyword, String contentKeyword);

	Page<Post> findPostsByTitleContainingOrContentContainingAndIsActiveTrueOrderByCreatedAtDesc(String titleKeyword, String contentKeyword, Pageable pageable);

	Page<Post> findPostsByUserAndIsActiveTrueOrderByCreatedAtDesc(User user, Pageable pageable);

	@Query(
		"SELECT new com.estsoft.estsoft2ndproject.domain.dto.admin.PostListResponse(p.postId, c.name, p.title, COUNT(cmt), u.nickname, p.viewCount, p.likeCount, p.createdAt, p.isActive) "
			+
			"FROM Post p " +
			"LEFT JOIN Category c ON p.targetId = c.id " +
			"LEFT JOIN Comment cmt ON cmt.post = p " +
			"LEFT JOIN User u ON u.userId = p.user.userId " +
			"GROUP BY p.postId, c.name, u.nickname " +
			"ORDER BY p.createdAt DESC")
	List<PostListResponse> findPostListWithCategoryAndCommentsOrderByCreatedAtDesc();

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
		@Param("postType") String postType, @Param("targetId") Long targetId, Pageable pageable);

	@Query(
		"SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.postType = :postType AND p.targetId = :targetId AND p.isActive = true "
			+
			"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast7Days(
		@Param("sevenDaysAgo") Timestamp sevenDaysAgo, @Param("postType") String postType, @Param("targetId") Long targetId,
		Pageable pageable);

	Page<Post> findPostsByIsActiveTrueAndPostTypeAndTargetIdOrderByCreatedAtDesc(String postType, Long TargetId, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :today AND p.postType = :postType AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast24HoursByPostType(@Param("today") Timestamp today, @Param("postType") String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.createdAt >= :sevenDaysAgo AND p.postType = :postType AND p.isActive = true " +
		"ORDER BY (p.viewCount * 2 + p.likeCount * 5) DESC")
	Page<Post> findTopPostsForLast7DaysByPostType(@Param("sevenDaysAgo") Timestamp sevenDaysAgo, @Param("postType") String postType, Pageable pageable);

	Page<Post> findPostsByPostTypeAndIsActiveTrueOrderByCreatedAtDesc(String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.isActive = true AND p.postType LIKE %:suffix ORDER BY p.createdAt DESC")
	Page<Post> findPostsByPostTypeSuffixAndIsActiveTrueOrderByCreatedAtDesc(
		@Param("suffix") String suffix,
		Pageable pageable
	);

	Page<Post> findByTitleContainingAndIsActiveTrueAndPostTypeAndTargetIdOrderByCreatedAtDesc(String title, String postType, Long targetId,
		Pageable pageable);

	Page<Post> findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeAndTargetIdOrderByCreatedAtDesc(String titleKeyword,
		String contentKeyword, String postType, Long targetId, Pageable pageable);

	Page<Post> findByTitleContainingAndIsActiveTrueAndPostTypeOrderByCreatedAtDesc(String title, String postType, Pageable pageable);

	Page<Post> findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeOrderByCreatedAtDesc(String titleKeyword,
		String contentKeyword, String postType, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.isActive = true AND p.title LIKE :title AND p.postType LIKE :suffix ORDER BY p.createdAt DESC")
	Page<Post> findByTitleContainingAndIsActiveTrueAndPostTypeSuffixOrderByCreatedAtDesc(
		@Param("title") String title,
		@Param("suffix") String suffix,
		Pageable pageable
	);

	@Query("SELECT p FROM Post p WHERE p.isActive = true AND (p.title LIKE :titleKeyword OR p.content LIKE :contentKeyword) AND p.postType LIKE :suffix ORDER BY p.createdAt DESC")
	Page<Post> findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeSuffixOrderByCreatedAtDesc(
		@Param("titleKeyword") String titleKeyword,
		@Param("contentKeyword") String contentKeyword,
		@Param("suffix") String suffix,
		Pageable pageable
	);

	@Query("SELECT p FROM Post p " +
		"WHERE (:searchType IS NULL OR " +
		"       (:searchType = 'title' AND p.title LIKE %:query%) OR " +
		"       (:searchType = 'user' AND p.user.nickname LIKE %:query%) OR " +
		"       (:searchType = 'content' AND (p.title LIKE %:query% OR p.content LIKE %:query%))) " +
		"AND (:postType IS NULL OR p.postType = :postType) " +
		"AND (:targetId IS NULL OR p.targetId = :targetId) " +
		"AND (:isActive IS NULL OR p.isActive = :isActive)")
	Page<Post> findFilteredPosts(@Param("searchType") String searchType,
		@Param("postType") String postType,
		@Param("targetId") Long targetId,
		@Param("isActive") Boolean isActive,
		@Param("query") String query,
		Pageable pageable);

	List<Post> findPostsByPostTypeAndTargetId(String postType, Long targetId);

	default Page<Post> searchAdminPosts(String searchType, String postType, String isActive, String query, Pageable pageable) {
		return findAll((root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (StringUtils.hasText(query) && StringUtils.hasText(searchType)) {
				switch (searchType) {
					case "title":
						predicates.add(criteriaBuilder.like(root.get("title"), "%" + query + "%"));
						break;
					case "user":
						predicates.add(criteriaBuilder.like(root.get("writer").get("nickname"), "%" + query + "%"));
						break;
					case "content":
						predicates.add(
							criteriaBuilder.or(
								criteriaBuilder.like(root.get("title"), "%" + query + "%"),
								criteriaBuilder.like(root.get("content"), "%" + query + "%")
							)
						);
						break;
				}
			}

			// postType 조건 추가
			if (postType != null && !"all".equalsIgnoreCase(postType)) {
				predicates.add(criteriaBuilder.equal(root.get("postType"), postType));
			}

			// isActive 조건 추가
			if (isActive != null && !"all".equalsIgnoreCase(isActive)) {
				predicates.add(criteriaBuilder.equal(root.get("isActive"), Boolean.parseBoolean(isActive)));
			}

			return predicates.isEmpty()
				? criteriaBuilder.conjunction()
				: criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		}, pageable);
	}
}
