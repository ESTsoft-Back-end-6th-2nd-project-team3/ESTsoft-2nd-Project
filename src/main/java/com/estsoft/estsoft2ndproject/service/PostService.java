package com.estsoft.estsoft2ndproject.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

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

	public PostService(PostRepository postRepository, UserRepository userRepository,
		CategoryRepository categoryRepository, RegionRepository regionRepository, LikesRepository likesRepository,
		CommentRepository commentRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.regionRepository = regionRepository;
		this.likesRepository = likesRepository;
		this.commentRepository = commentRepository;
	}

	@Transactional
	public Post createPost(PostRequestDTO postRequestDTO, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return postRepository.save(postRequestDTO.toEntity(user));
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
				default -> postRepository.findByPostTypeAndIsActiveTrue(postType);
			};
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid post type provided: " + postType, e);
		}
	}

	public List<Post> getAllPosts() {
		return postRepository.findAllByIsActiveTrue();
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
	}

	public void UnLike(Long postId, Long userId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		post.setLikeCount((post.getLikeCount() - 1));
		postRepository.save(post);

		Likes likes = likesRepository.findByTargetIdAndUser_UserId(postId, userId);
		likesRepository.delete(likes);
	}

	public boolean getIsLiked(Long postId, Long userId) {
		Likes existingLike = likesRepository.findByTargetIdAndUser_UserId(postId, userId);
		return existingLike != null;
	}

	public List<Post> searchPostsByKeyword(String keyword) {
		return postRepository.findByTitleContainingOrContentContainingAndIsActiveTrue(keyword, keyword);
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
		LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
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

	public List<SubMenu> getSubMenus(String level) {
		List<String> categories = getCategoryList().stream().map(Category::getName).toList();
		List<Long> categoryIds = getCategoryList().stream().map(Category::getId).toList();
		List<String> categoryUrls = categories.isEmpty() ? Collections.emptyList() :
			categoryIds.stream().map(categoryId -> "/category?id=" + categoryId).toList();

		List<String> regions = getRegionList().stream().map(Region::getName).toList();
		List<Long> regionIds = getRegionList().stream().map(Region::getId).toList();
		List<String> regionUrls = regions.isEmpty() ? Collections.emptyList() :
			regionIds.stream().map(regionId -> "/region?id=" + regionId).toList();

		List<SubMenu> subMenus = new ArrayList<>();
		subMenus.add(new SubMenu("카테고리", categories, null, categoryUrls));
		subMenus.add(new SubMenu("챌린지", null, "/challenge", null));
		subMenus.add(new SubMenu("지역 친목 게시판", regions, null, regionUrls));
		subMenus.add(new SubMenu("마이페이지", null, "/mypage", null));

		if (level.equals("관리자")) {
			subMenus.add(3, new SubMenu("관리자 메뉴", null, "/admin", null));
		}

		return subMenus;
	}

	public List<PostResponseDTO> getTodayBestPostByPostTypeAndTargetId(String postType, Long targetId) {
		LocalDateTime today = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
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
		LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
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
		Page<Post> postPage = postRepository.findPostsByIsActiveTrueAndPostTypeAndTargetId(postType, targetId,
			pageRequest);

		return postPage.map(post -> {
			PostResponseDTO postResponseDTO = new PostResponseDTO(post);
			postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
			postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));
			return postResponseDTO;
		});
	}

	public List<PostResponseDTO> getTodayBestChallengePost() {
		LocalDateTime today = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
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
		LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
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
			postPage = postRepository.findPostsByPostTypeSuffixAndIsActiveTrue("CHALLENGE", pageRequest);
		} else {
			postPage = postRepository.findPostsByPostTypeAndIsActiveTrue(postType, pageRequest);
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

	public PostResponseDTO getPostDetail(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		PostResponseDTO postResponseDTO = new PostResponseDTO(post);
		postResponseDTO.setCommentCount(getCommentCount(post.getPostId()));
		postResponseDTO.setNickname(getNicknameByPostId(post.getPostId()));

		Boolean isLiked = getIsLiked(postId, 1L);
		postResponseDTO.setIsLiked(isLiked);

		return postResponseDTO;
	}

	public Boolean isAdmin(CustomUserDetails userDetails) {
		return userDetails.getUser().getLevel().equals("관리자");
	}
}