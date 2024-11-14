package com.estsoft.estsoft2ndproject.service;

import static java.util.Arrays.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.estsoft.estsoft2ndproject.domain.ActivityScore;
import com.estsoft.estsoft2ndproject.domain.Category;
import com.estsoft.estsoft2ndproject.domain.Likes;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.PostType;
import com.estsoft.estsoft2ndproject.domain.Region;
import com.estsoft.estsoft2ndproject.domain.SubMenu;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.exception.PostNotFoundException;
import com.estsoft.estsoft2ndproject.exception.UserNotFoundException;
import com.estsoft.estsoft2ndproject.repository.ActivityScoreRepository;
import com.estsoft.estsoft2ndproject.repository.CategoryRepository;
import com.estsoft.estsoft2ndproject.repository.CommentRepository;
import com.estsoft.estsoft2ndproject.repository.LikesRepository;
import com.estsoft.estsoft2ndproject.repository.PostRepository;
import com.estsoft.estsoft2ndproject.repository.RegionRepository;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final RegionRepository regionRepository;
	private final LikesRepository likesRepository;
	private final CommentRepository commentRepository;
	private final ActivityScoreRepository activityScoreRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository,
		CategoryRepository categoryRepository, RegionRepository regionRepository, LikesRepository likesRepository,
		CommentRepository commentRepository, ActivityScoreRepository activityScoreRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.regionRepository = regionRepository;
		this.likesRepository = likesRepository;
		this.commentRepository = commentRepository;
		this.activityScoreRepository = activityScoreRepository;
	}

	@Transactional
	public Post createPost(PostRequestDTO postRequestDTO, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		updateActivityScore(user, 2, "게시글 작성");
		return postRepository.save(postRequestDTO.toEntity(user));
	}

	public void updateActivityScore(User user, int scoreFluctuations, String reason) {
		// activity_score 테이블에 기록 추가
		ActivityScore activityScore = new ActivityScore();
		activityScore.setUser(user);
		activityScore.setScoreFluctuations(scoreFluctuations);
		activityScore.setFluctuationAt(new Timestamp(System.currentTimeMillis()));
		activityScore.setFluctuationReason(reason);
		activityScoreRepository.save(activityScore);

		// user 테이블의 activity_score 필드 업데이트
		user.setActivityScore(user.getActivityScore() + scoreFluctuations);
		userRepository.save(user);
	}

	@Transactional
	public Post getPostById(Long postId) {
		return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
	}

	public Post updatePost(Long postId, PostRequestDTO postRequestDTO) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Post not found"));
		post.UpdatePost(postRequestDTO.getTitle(), postRequestDTO.getContent(), postRequestDTO.getPostType(),
			postRequestDTO.getTargetId());
		return postRepository.save(post);
	}

	public List<Category> getCategoryList() {
		return categoryRepository.findAll();
	}

	public List<Region> getRegionList() {
		return regionRepository.findAll();
	}

	public String getCategoryByPostType(String postType, Long targetId) {
		try {
			PostType postTypeEnum = PostType.valueOf(postType);
			return switch (postTypeEnum) {
				case PARTICIPATION_CATEGORY -> categoryRepository.findById(targetId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"))
					.getName();
				case PARTICIPATION_REGION -> regionRepository.findById(targetId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid region ID"))
					.getName();
				default -> "";
			};

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid post type provided: " + postType, e);
		}
	}

	public List<Post> getPostsByCategory(String postType, Long targetId) {
		try {
			PostType postTypeEnum = PostType.valueOf(postType);
			return switch (postTypeEnum) {
				case PARTICIPATION_CATEGORY, PARTICIPATION_REGION -> postRepository.findByPostTypeAndTargetIdAndIsActiveTrue(postType, targetId);
				default -> postRepository.findByPostTypeAndIsActiveTrueOrderByCreatedAtDesc(postType);
			};
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid post type provided: " + postType, e);
		}
	}

	public List<Post> getAllPosts() {
		return postRepository.findAllByIsActiveTrueOrderByCreatedAtDesc();
	}

	public void deletePost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		post.setIsActive(false);
		postRepository.save(post);
	}

	public void increaseViewCount(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		post.setViewCount(post.getViewCount() + 1);
		postRepository.save(post);
	}

	public void Like(Long postId, Long userId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		post.setLikeCount((post.getLikeCount() + 1));
		postRepository.save(post);

		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		Likes likes = new Likes(post.getPostType(), postId, user);
		likesRepository.save(likes);

		User postOwner = post.getUser();
		updateActivityScore(postOwner, 5, "게시글 추천");
	}

	public void UnLike(Long postId, Long userId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		post.setLikeCount((post.getLikeCount() - 1));
		postRepository.save(post);

		Likes likes = likesRepository.findByTargetIdAndUser_UserId(postId, userId);
		likesRepository.delete(likes);

		User postOwner = post.getUser();
		updateActivityScore(postOwner, -5, "게시글 추천 취소");
	}

	public boolean getIsLiked(Long postId, Long userId) {
		Likes existingLike = likesRepository.findByTargetIdAndUser_UserId(postId, userId);
		return existingLike != null;
	}

	public List<Post> searchPostsByKeyword(String keyword) {
		return postRepository.findByTitleContainingOrContentContainingAndIsActiveTrueOrderByCreatedAtDesc(keyword, keyword);
	}

	public Post getNoticeTop1() {
		return postRepository.findTop1ByPostTypeOrderByCreatedAtDesc(PostType.ANNOUNCEMENT.toString());
	}

	public Post getChallengeNoticeTop1() {
		return postRepository.findTop1ByPostTypeOrderByCreatedAtDesc(PostType.GENERATION_CHALLENGE.toString());
	}

	public Integer getCommentCount(Long postId) {
		return commentRepository.countByPost_PostIdAndIsActiveTrue(postId);
	}

	public List<PostResponseDTO> getWeeklyBestPost() {
		Timestamp sevenDaysAgo = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
		// LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<Post> topPosts = postRepository.findTop5PostsByLast7DaysSortedByViewsAndLikes(sevenDaysAgo,
			pageRequest);
		List<Post> postList = topPosts.getContent();
		return postList.stream()
			.map(post -> {
				PostResponseDTO postResponseDTO = new PostResponseDTO(post);
				postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
				return postResponseDTO;
			})
			.toList();
	}

	public List<SubMenu> getSubMenus(String level, Long userId) {
		List<String> categories = getCategoryList().stream().map(Category::getName).toList();
		List<Long> categoryIds = getCategoryList().stream().map(Category::getId).toList();
		List<String> categoryUrls = categories.isEmpty() ? Collections.emptyList() :
			categoryIds.stream().map(categoryId -> "/category?id=" + categoryId).toList();

		List<String> regions = getRegionList().stream().map(Region::getName).toList();
		List<Long> regionIds = getRegionList().stream().map(Region::getId).toList();
		List<String> regionUrls = regions.isEmpty() ? Collections.emptyList() :
			regionIds.stream().map(regionId -> "/region?id=" + regionId).toList();

		List<String> admins = asList("게시글 관리", "유저 관리");
		List<Long> adminIds = asList(1L, 2L);
		List<String> adminUrls = adminIds.stream().map(adminId -> "/admin?id=" + adminId).toList();

		List<SubMenu> subMenus = new ArrayList<>();
		subMenus.add(new SubMenu("카테고리", categories, null, categoryUrls));
		subMenus.add(new SubMenu("챌린지", null, "/challenge", null));
		subMenus.add(new SubMenu("지역 친목 게시판", regions, null, regionUrls));
		subMenus.add(new SubMenu("마이페이지", null, "/mypage/" + userId, null));

		if (level.equals("관리자")) {
			subMenus.add(3, new SubMenu("관리자 메뉴", admins, null, adminUrls));
		}

		return subMenus;
	}

	public List<PostResponseDTO> getTodayBestPostByPostTypeAndTargetId(String postType, Long targetId) {
		Timestamp today = Timestamp.valueOf(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<Post> topPosts = postRepository.findTopPostsForLast24Hours(today, postType, targetId, pageRequest);
		List<Post> postList = topPosts.getContent();
		if (postList.isEmpty()) {
			return getWeeklyBestPostByPostTypeAndTargetId(postType, targetId);
		}
		return postList.stream()
			.map(post -> {
				PostResponseDTO postResponseDTO = new PostResponseDTO(post);
				postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
				return postResponseDTO;
			})
			.toList();
	}

	public List<PostResponseDTO> getWeeklyBestPostByPostTypeAndTargetId(String postType, Long targetId) {
		Timestamp sevenDaysAgo = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<Post> topPosts = postRepository.findTopPostsForLast7Days(sevenDaysAgo, postType, targetId,
			pageRequest);
		List<Post> postList = topPosts.getContent();
		return postList.stream()
			.map(post -> {
				PostResponseDTO postResponseDTO = new PostResponseDTO(post);
				postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
				return postResponseDTO;
			})
			.toList();
	}

	public Page<PostResponseDTO> getPaginationPostsByPostTypeAndTargetId(String postType, Long targetId, int page,
		int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Post> postPage = postRepository.findPostsByIsActiveTrueAndPostTypeAndTargetIdOrderByCreatedAtDesc(postType, targetId,
			pageRequest);

		return postPage.map(post -> {
			PostResponseDTO postResponseDTO = new PostResponseDTO(post);
			postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
			postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));
			return postResponseDTO;
		});
	}

	public List<PostResponseDTO> getTodayBestChallengePost() {
		Timestamp today = Timestamp.valueOf(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<Post> topPosts = postRepository.findTopPostsForLast24HoursByPostType(today,
			PostType.PARTICIPATION_CHALLENGE.toString(), pageRequest);
		List<Post> postList = topPosts.getContent();
		if (postList.isEmpty()) {
			return getWeeklyBestChallengePost();
		}
		return postList.stream()
			.map(post -> {
				PostResponseDTO postResponseDTO = new PostResponseDTO(post);
				postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
				return postResponseDTO;
			})
			.toList();
	}

	public List<PostResponseDTO> getWeeklyBestChallengePost() {
		Timestamp sevenDaysAgo = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
		PageRequest pageRequest = PageRequest.of(0, 5);
		Page<Post> topPosts = postRepository.findTopPostsForLast7DaysByPostType(sevenDaysAgo,
			PostType.PARTICIPATION_CHALLENGE.toString(), pageRequest);
		List<Post> postList = topPosts.getContent();
		return postList.stream()
			.map(post -> {
				PostResponseDTO postResponseDTO = new PostResponseDTO(post);
				postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
				return postResponseDTO;
			})
			.toList();
	}

	public Page<PostResponseDTO> getPaginationPostsByPostType(String postType, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Post> postPage;
		if (postType.equals(PostType.PARTICIPATION_CHALLENGE.toString())) {
			postPage = postRepository.findPostsByPostTypeSuffixAndIsActiveTrueOrderByCreatedAtDesc("CHALLENGE", pageRequest);
		} else {
			postPage = postRepository.findPostsByPostTypeAndIsActiveTrueOrderByCreatedAtDesc(postType, pageRequest);
		}

		return postPage.map(post -> {
			PostResponseDTO postResponseDTO = new PostResponseDTO(post);
			postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
			postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));
			return postResponseDTO;
		});
	}

	public List<String> getEtcList(String level, String postType) {
		if (postType.equals(PostType.ANNOUNCEMENT.toString())) {
			return Arrays.asList(PostType.ANNOUNCEMENT.toString());
		} else if (level.equals("관리자")) {
			return Arrays.asList(PostType.GENERATION_CHALLENGE.toString());
		} else {
			return Arrays.asList(PostType.PARTICIPATION_CHALLENGE.toString());
		}
	}

	public String getNicknameByPostId(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		return post.getUser().getNickname();
	}

	public PostResponseDTO getPostDetail(Long postId, Long userId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		PostResponseDTO postResponseDTO = new PostResponseDTO(post);
		postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
		postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));

		Boolean isLiked = getIsLiked(postId, userId);
		postResponseDTO.setIsLiked(isLiked);

		return postResponseDTO;
	}

	public Boolean isAdmin(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return userDetails.getUser().getLevel().equals("관리자");
	}

	public Page<PostResponseDTO> searchPostsByTitle(String keyword, String postType, Long targetId, int page,
		int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Post> postPage;
		if (targetId != null) {
			postPage = postRepository.findByTitleContainingAndIsActiveTrueAndPostTypeAndTargetIdOrderByCreatedAtDesc(keyword,
				postType,
				targetId,
				pageRequest);
		} else {
			if (postType.equals(PostType.PARTICIPATION_CHALLENGE.toString()) || postType.equals(
				PostType.GENERATION_CHALLENGE.toString())) {
				postPage = postRepository.findByTitleContainingAndIsActiveTrueAndPostTypeSuffixOrderByCreatedAtDesc("%" + keyword + "%",
					"%CHALLENGE",
					pageRequest);
			} else {
				postPage = postRepository.findByTitleContainingAndIsActiveTrueAndPostTypeOrderByCreatedAtDesc(keyword, postType,
					pageRequest);
			}
		}

		return postPage.map(post -> {
			PostResponseDTO postResponseDTO = new PostResponseDTO(post);
			postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
			postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));
			return postResponseDTO;
		});
	}

	public Page<PostResponseDTO> searchPostsByTitleOrContent(String keyword, String postType, Long targetId, int page,
		int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Post> postPage;
		if (targetId != null) {
			postPage = postRepository.findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeAndTargetIdOrderByCreatedAtDesc(
				keyword, keyword, postType, targetId, pageRequest);
		} else {
			if (postType.equals(PostType.PARTICIPATION_CHALLENGE.toString()) || postType.equals(
				PostType.GENERATION_CHALLENGE.toString())) {
				postPage = postRepository.findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeSuffixOrderByCreatedAtDesc(
					"%" + keyword + "%", "%" + keyword + "%", "%CHALLENGE", pageRequest);
			} else {
				postPage = postRepository.findByTitleContainingOrContentContainingAndIsActiveTrueAndPostTypeOrderByCreatedAtDesc(
					keyword, keyword, postType, pageRequest);
			}
		}

		return postPage.map(post -> {
			PostResponseDTO postResponseDTO = new PostResponseDTO(post);
			postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
			postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));
			return postResponseDTO;
		});
	}

	// 오늘의 베스트 게시글 (24시간 기준)
	public List<PostResponseDTO> getTodayTopLikedPosts() {
		// 현재 시간 기준 24시간 전과 현재 시간 계산
		Timestamp startOfLast24Hours = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
		Timestamp endOfCurrentTime = Timestamp.valueOf(LocalDateTime.now());

		// LikesRepository에서 데이터 조회
		List<Object[]> results = likesRepository.findTopLikedPostsToday(startOfLast24Hours, endOfCurrentTime);

		// PostRepository를 사용해 게시글 데이터 변환 및 DTO 생성
		return results.stream()
			.map(result -> {
				Post post = postRepository.findById((Long)result[0])
					.orElseThrow(() -> new RuntimeException("Post not found"));

				// PostResponseDTO 생성
				PostResponseDTO dto = new PostResponseDTO(post);

				// postType별로 한글 이름 설정
				if (post.getPostType().equals("PARTICIPATION_REGION")) {
					// target_id를 사용해 region DB에서 name 조회
					String regionName = regionRepository.findById(post.getTargetId())
						.map(Region::getName)
						.orElse("알 수 없음");
					dto.setPostTypeKorean(regionName);
				} else if (post.getPostType().equals("PARTICIPATION_CATEGORY")) {
					// target_id를 사용해 category DB에서 name 조회
					String categoryName = categoryRepository.findById(post.getTargetId())
						.map(Category::getName)
						.orElse("알 수 없음");
					dto.setPostTypeKorean(categoryName);
				} else {
					// ENUM을 사용해 한글 이름 변환
					dto.setPostTypeKorean(PostType.getKoreanNameByString(post.getPostType()));
				}

				return dto;
			})
			.collect(Collectors.toList());
	}

	// 이달의 활동왕
	public List<User> getMonthlyTopUsers() {
		Timestamp startDate = Timestamp.valueOf(LocalDate.now().minusDays(30).atStartOfDay());
		Timestamp endDate = Timestamp.valueOf(LocalDateTime.now());

		List<Object[]> results = activityScoreRepository.findMonthlyTopUsers(startDate, endDate);

		return results.stream()
			.map(result -> {
				User user = new User();
				user.setUserId((Long)result[0]);
				user.setNickname((String)result[1]);
				user.setLevel((String)result[2]);
				user.setActivityScore(((Number)result[3]).intValue());
				return user;
			})
			.toList();
	}

	// 모든 활성화된 게시글 가져오기 (DTO로 변환)
	public List<PostResponseDTO> getAllActivePostsAsDTO() {
		List<Post> activePosts = postRepository.findAllByIsActiveTrueOrderByCreatedAtDesc();

		return activePosts.stream().map(post -> {
			PostResponseDTO dto = new PostResponseDTO();
			dto.setPostId(post.getPostId());
			dto.setTitle(post.getTitle());
			dto.setViewCount(post.getViewCount());
			dto.setTargetId(post.getTargetId());

			// target_id가 null인지 확인
			if (post.getTargetId() == null) {
				// target_id가 없으면 enum의 한글 이름 사용
				dto.setPostType(post.getPostType());
				dto.setPostTypeKorean(PostType.getKoreanNameByString(post.getPostType()));
			} else {
				// target_id가 있을 경우 region 또는 category에서 이름 조회
				if (post.getPostType().equals(PostType.PARTICIPATION_REGION.name())) {
					Region region = regionRepository.findById(post.getTargetId())
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역 ID입니다."));
					dto.setPostType(post.getPostType());
					dto.setPostTypeKorean(region.getName());
				} else if (post.getPostType().equals(PostType.PARTICIPATION_CATEGORY.name())) {
					Category category = categoryRepository.findById(post.getTargetId())
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));
					dto.setPostType(post.getPostType());
					dto.setPostTypeKorean(category.getName());
				} else {
					// 기타 경우: enum의 한글 이름 사용
					dto.setPostType(post.getPostType());
					dto.setPostTypeKorean(PostType.getKoreanNameByString(post.getPostType()));
				}
			}

			return dto;
		}).collect(Collectors.toList());
	}

	// 특정 타입의 게시글 필터링
	public List<PostResponseDTO> filterPostsByType(List<PostResponseDTO> posts, List<String> types) {
		return posts.stream()
			.filter(post -> types.contains(post.getPostType()))
			.toList();
	}

	public String getDisplayName(String postType, Long targetId) {
		if (postType == null || postType.isEmpty()) {
			return "Unknown"; // 기본값 처리
		}

		try {
			PostType postTypeEnum = PostType.valueOf(postType);
			return switch (postTypeEnum) {
				case PARTICIPATION_CATEGORY -> categoryRepository.findById(targetId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"))
					.getName();
				case PARTICIPATION_REGION -> regionRepository.findById(targetId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid region ID"))
					.getName();
				case PARTICIPATION_CHALLENGE, GENERATION_CHALLENGE -> "Challenge";
				case ANNOUNCEMENT -> "Announcement";
				default -> "Other";
			};
		} catch (IllegalArgumentException e) {
			return "Unknown"; // 예외 발생 시 기본값 반환
		}
	}

	public List<PostResponseDTO> convertToDTO(List<Post> posts) {
		return posts.stream()
			.map(post -> {
				String displayName = getDisplayName(post.getPostType(), post.getTargetId());
				return new PostResponseDTO(
					post.getPostId(),
					post.getTitle(),
					post.getPostType(),
					post.getTargetId(),
					displayName,
					post.getViewCount(),
					post.getCreatedAt()
				);
			})
			.toList();
	}

	public Page<PostResponseDTO> getPaginationPostsByKeyword(String keyword, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Post> postPage;

		postPage = postRepository.findPostsByTitleContainingOrContentContainingAndIsActiveTrueOrderByCreatedAtDesc(keyword, keyword, pageRequest);

		return postPage.map(post -> {
			PostResponseDTO postResponseDTO = new PostResponseDTO(post);
			postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
			postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));
			return postResponseDTO;
		});
	}

	public Page<PostResponseDTO> getPaginationPostsByUser(User user, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Post> postPage;

		postPage = postRepository.findPostsByUserAndIsActiveTrueOrderByCreatedAtDesc(user, pageRequest);

		return postPage.map(post -> {
			PostResponseDTO postResponseDTO = new PostResponseDTO(post);
			postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
			postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));
			return postResponseDTO;
		});
	}
}