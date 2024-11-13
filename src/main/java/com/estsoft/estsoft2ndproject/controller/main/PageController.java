package com.estsoft.estsoft2ndproject.controller.main;

import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.SubMenu;
import com.estsoft.estsoft2ndproject.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.estsoft.estsoft2ndproject.domain.Category;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.PostType;
import com.estsoft.estsoft2ndproject.domain.Region;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.repository.UserRepository;
import com.estsoft.estsoft2ndproject.service.AdminService;
import com.estsoft.estsoft2ndproject.service.CommentService;
import com.estsoft.estsoft2ndproject.service.MyPageService;
import com.estsoft.estsoft2ndproject.service.ObjectiveService;
import com.estsoft.estsoft2ndproject.service.PostService;
import com.estsoft.estsoft2ndproject.service.UserService;

import lombok.AllArgsConstructor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class PageController {
	private final PostService postService;
	private final CommentService commentService;
	private final UserRepository userRepository;
	private final UserService userService;
	private final ObjectiveService objectiveService;
	private final MyPageService myPageService;
	private final AdminService adminService;

	@GetMapping("/")
	public String menuPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		addMenuData(model, userDetails);

		try {
			// 모든 활성 게시글 가져오기
			List<PostResponseDTO> allPostsDTO = postService.getAllActivePostsAsDTO();
			if (allPostsDTO == null || allPostsDTO.isEmpty()) {
				throw new Exception("활성 게시글이 없습니다.");
			}

			// 게시글 필터링
			List<PostResponseDTO> regionAndCategoryPosts = postService.filterPostsByType(
				allPostsDTO, List.of("PARTICIPATION_CATEGORY", "PARTICIPATION_REGION")
			);
			List<PostResponseDTO> challengePosts = postService.filterPostsByType(
				allPostsDTO, List.of("PARTICIPATION_CHALLENGE", "GENERATION_CHALLENGE")
			);

			// 필터링된 리스트가 null인 경우 기본값 설정
			regionAndCategoryPosts = regionAndCategoryPosts != null ? regionAndCategoryPosts : Collections.emptyList();
			challengePosts = challengePosts != null ? challengePosts : Collections.emptyList();

			// 게시글 나누기
			model.addAttribute("latestPosts", getSubList(regionAndCategoryPosts, 0, 20));
			model.addAttribute("olderPosts", getSubList(regionAndCategoryPosts, 20, 40));

			// 챌린지 게시글 나누기
			model.addAttribute("latestChallenges", getSubList(challengePosts, 0, 20));
			model.addAttribute("olderChallenges", getSubList(challengePosts, 20, 40));
		} catch (Exception e) {
			// 오류 처리 및 기본값 설정
			model.addAttribute("latestPosts", Collections.emptyList());
			model.addAttribute("olderPosts", Collections.emptyList());
			model.addAttribute("latestChallenges", Collections.emptyList());
			model.addAttribute("olderChallenges", Collections.emptyList());
			model.addAttribute("error", "게시판 데이터를 로드하는 중 오류가 발생했습니다.");
			e.printStackTrace(); // 로그 남기기
		}

		addUserDetailsToModel(model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		model.addAttribute("mainFragment1", "fragment/board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	private void addMenuData(Model model, CustomUserDetails userDetails) {
		String level = "";
		Long userId = null;

		if (userDetails != null) {
			User user = userDetails.getUser();

			level = user.getLevel();
			userId = user.getUserId();
		}

		model.addAttribute("subMenus", postService.getSubMenus(level, userId));
	}

	private void addCategoryNamePageData(Model model) {
		Post notice = postService.getNoticeTop1();
		Post challenge = postService.getChallengeNoticeTop1();
		User noticeUser = notice.getUser();
		User challengeUser = challenge.getUser();

		model.addAttribute("notice", notice);
		model.addAttribute("noticeUser", noticeUser);
		model.addAttribute("challenge", challenge);
		model.addAttribute("challengeUser", challengeUser);
	}

	private void addCategoryPageData(Long categoryId, int page, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.PARTICIPATION_CATEGORY.getKoreanName() + " > " + postService.getCategoryByPostType(
			PostType.PARTICIPATION_CATEGORY.toString(), categoryId);

		List<PostResponseDTO> todayBest = postService.getTodayBestPostByPostTypeAndTargetId(
			PostType.PARTICIPATION_CATEGORY.toString(), categoryId);
		List<PostResponseDTO> weeklyBest = postService.getWeeklyBestPostByPostTypeAndTargetId(
			PostType.PARTICIPATION_CATEGORY.toString(), categoryId);
		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostTypeAndTargetId(
			PostType.PARTICIPATION_CATEGORY.toString(), categoryId, page, 30);

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.PARTICIPATION_CATEGORY.toString());
		model.addAttribute("targetId", categoryId);
		model.addAttribute("todayBest", todayBest);
		model.addAttribute("weeklyBest", weeklyBest);
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
	}

	private void addRegionPageData(Long regionId, int page, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		String boardName = PostType.PARTICIPATION_REGION.getKoreanName() + " > " + postService.getCategoryByPostType(
			PostType.PARTICIPATION_REGION.toString(), regionId);

		List<PostResponseDTO> todayBest = postService.getTodayBestPostByPostTypeAndTargetId(
			PostType.PARTICIPATION_REGION.toString(), regionId);
		List<PostResponseDTO> weeklyBest = postService.getWeeklyBestPostByPostTypeAndTargetId(
			PostType.PARTICIPATION_REGION.toString(), regionId);
		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostTypeAndTargetId(
			PostType.PARTICIPATION_REGION.toString(), regionId, page, 30);

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.PARTICIPATION_REGION.toString());
		model.addAttribute("targetId", regionId);
		model.addAttribute("todayBest", todayBest);
		model.addAttribute("weeklyBest", weeklyBest);
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
	}

	@GetMapping("/category")
	public String categoryPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(name = "id") Long categoryId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addCategoryPageData(categoryId, page, model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		addUserDetailsToModel(model, userDetails);

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/region")
	public String regionPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(name = "id") Long regionId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addRegionPageData(regionId, page, model, userDetails);
		addUserDetailsToModel(model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/challenge")
	public String challengePage(@RequestParam(defaultValue = "0", name = "page") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.PARTICIPATION_CHALLENGE.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		List<PostResponseDTO> todayBest = postService.getTodayBestChallengePost();
		List<PostResponseDTO> weeklyBest = postService.getWeeklyBestChallengePost();
		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostType(
			PostType.PARTICIPATION_CHALLENGE.toString(), page, 30);

		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.PARTICIPATION_CHALLENGE.toString());
		model.addAttribute("todayBest", todayBest);
		model.addAttribute("weeklyBest", weeklyBest);
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		addUserDetailsToModel(model, userDetails);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/announcement")
	public String announcementPage(@RequestParam(defaultValue = "0", name = "page") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.ANNOUNCEMENT.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostType(PostType.ANNOUNCEMENT.toString(),
			page, 30);

		addUserDetailsToModel(model, userDetails);

		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.ANNOUNCEMENT.toString());
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/write")
	public String writeCategoryPost(@RequestParam(required = false, name = "postId") Long postId,
		@RequestParam(name = "postType") String postType,
		@RequestParam(name = "targetId") Long targetId, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = postService.getCategoryByPostType(postType, targetId);

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<Category> categories = null;
		List<Region> regions = null;

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		if (postType.equals(PostType.PARTICIPATION_CATEGORY.toString())) {
			categories = postService.getCategoryList();
		} else if (postType.equals(PostType.PARTICIPATION_REGION.toString())) {
			regions = postService.getRegionList();
		}

		if (postId == null) {
			model.addAttribute("post", new PostResponseDTO());
			model.addAttribute("selectedPostType", postType);
			model.addAttribute("selectedCategory", targetId);
		} else {
			Post post = postService.getPostById(postId);
			model.addAttribute("post", new PostResponseDTO(post));
			model.addAttribute("selectedPostType", post.getPostType());
			model.addAttribute("selectedCategory", post.getTargetId());
		}

		addUserDetailsToModel(model, userDetails);

		model.addAttribute("categoryName", boardName);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("categories", categories);
		model.addAttribute("regions", regions);
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/write-post");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/writeChallenge")
	public String writeChallengePost(@RequestParam(required = false, name = "postId") Long postId,
		@RequestParam(name = "postType") String postType, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.getKoreanNameByString(postType);

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		List<String> etcList = postService.getEtcList(userDetails.getUser().getLevel(), postType);
		List<PostType> filteredPostTypes = etcList.stream()
			.map(PostType::valueOf)
			.toList();

		if (postId == null) {
			model.addAttribute("post", new PostResponseDTO());
			model.addAttribute("selectedPostType", filteredPostTypes.get(0));
		} else {
			Post post = postService.getPostById(postId);
			model.addAttribute("post", new PostResponseDTO(post));
			model.addAttribute("selectedPostType", post.getPostType());
		}

		addUserDetailsToModel(model, userDetails);

		model.addAttribute("categoryName", boardName);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("postTypes", filteredPostTypes);
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/write-post");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/category/post/{postId}")
	public String categoryPostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(name = "targetId") Long categoryId,
		@RequestParam(defaultValue = "0", name = "page") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addCategoryPageData(categoryId, page, model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		postService.increaseViewCount(postId);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("post", post);
		model.addAttribute("postId", postId);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("comments", commentList);
		model.addAttribute("commentCount", commentService.getCommentCountByPostId(postId));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/view-post");
		model.addAttribute("mainFragment3", "fragment/view-comment");
		model.addAttribute("mainFragment4", "fragment/category-name");
		model.addAttribute("mainFragment5", "fragment/category-best");
		model.addAttribute("mainFragment6", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/region/post/{postId}")
	public String regionPostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(name = "targetId") Long regionId,
		@RequestParam(defaultValue = "0", name = "page") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addRegionPageData(regionId, page, model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		postService.increaseViewCount(postId);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("post", post);
		model.addAttribute("postId", postId);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("comments", commentList);
		model.addAttribute("commentCount", commentService.getCommentCountByPostId(postId));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/view-post");
		model.addAttribute("mainFragment3", "fragment/view-comment");
		model.addAttribute("mainFragment4", "fragment/category-name");
		model.addAttribute("mainFragment5", "fragment/category-best");
		model.addAttribute("mainFragment6", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/challenge/post/{postId}")
	public String challengePostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(defaultValue = "0", name = "page") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.PARTICIPATION_CHALLENGE.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		postService.increaseViewCount(postId);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);
		List<PostResponseDTO> todayBest = postService.getTodayBestChallengePost();
		List<PostResponseDTO> weeklyBest = postService.getWeeklyBestChallengePost();
		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostType(
			PostType.PARTICIPATION_CHALLENGE.toString(), page, 30);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.PARTICIPATION_CHALLENGE.toString());
		model.addAttribute("post", post);
		model.addAttribute("postId", postId);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("comments", commentList);
		model.addAttribute("commentCount", commentService.getCommentCountByPostId(postId));
		model.addAttribute("todayBest", todayBest);
		model.addAttribute("weeklyBest", weeklyBest);
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/view-post");
		model.addAttribute("mainFragment3", "fragment/view-comment");
		model.addAttribute("mainFragment4", "fragment/category-name");
		model.addAttribute("mainFragment5", "fragment/category-best");
		model.addAttribute("mainFragment6", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/announcement/post/{postId}")
	public String announcementPostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(defaultValue = "0", name = "page") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.ANNOUNCEMENT.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);
		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostType(PostType.ANNOUNCEMENT.toString(),
			page, 30);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.ANNOUNCEMENT.toString());
		model.addAttribute("post", post);
		model.addAttribute("postId", postId);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("comments", commentList);
		model.addAttribute("commentCount", commentService.getCommentCountByPostId(postId));
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/view-post");
		model.addAttribute("mainFragment3", "fragment/view-comment");
		model.addAttribute("mainFragment4", "fragment/category-name");
		model.addAttribute("mainFragment5", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/member/register")
	public String register(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		addMenuData(model, null);

		String email = (String)session.getAttribute("email");
		String nickname = (String)session.getAttribute("nickname");
		String profileImageUrl = (String)session.getAttribute("profileImageUrl");

		model.addAttribute("email", email);
		model.addAttribute("nickname", nickname);
		model.addAttribute("profileImageUrl", profileImageUrl);
		model.addAttribute("mainFragment1", "fragment/register");

		return "index";
	}

	@GetMapping("/search")
	public String searchAllPage(Model model, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(name = "keyword") String keyword,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		Page<PostResponseDTO> postPage = postService.getPaginationPostsByKeyword(keyword, page, 30);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("keyword", keyword);
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/search-all");
		model.addAttribute("mainFragment2", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/mypage/written")
	public String myPageWrittenPosts(Model model, @RequestParam(defaultValue = "0", name = "page") int page, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		Page<PostResponseDTO> postPage = postService.getPaginationPostsByUser(userDetails.getUser(), page, 30);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("categoryName", "작성한 글");
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/mypage/{userId}")
	public String showMyPage(@PathVariable(name = "userId") Long userId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		addMenuData(model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		User user = userService.getUserById(userId);

		int month = LocalDate.now().getMonthValue();

		List<Objective> myObjective = objectiveService.getObjectivesForUserAndMonth(userId, LocalDate.now().withDayOfMonth(1));

		int completedCount = (int)myObjective.stream().filter(Objective::getIsCompleted).count();
		int totalCount = myObjective.size();
		int progressValue = totalCount > 0 ? (completedCount * 100) / totalCount : 0;
		addUserDetailsToModel(model, userDetails);

		model.addAttribute("user", user);
		model.addAttribute("month", month);
		model.addAttribute("progressValue", progressValue);
		model.addAttribute("myObjective", myObjective);
		model.addAttribute("objectiveData", myPageService.getMonthlyCompletionStats(userId));
		model.addAttribute("isOwner", userId.equals(userDetails.getUser().getUserId()));
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));

		model.addAttribute("mainFragment1", "fragment/mypage-profile");
		model.addAttribute("mainFragment2", "fragment/my-objective");
		model.addAttribute("mainFragment3", "fragment/profile-manage");
		model.addAttribute("mainFragment4", "fragment/profile-delete-user");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");

		return "index";
	}

	@GetMapping("/mypage/edit-profile")
	public String showEditProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();

		User user = userService.getUserById(userId);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		model.addAttribute("userId", user.getUserId());
		model.addAttribute("profileImageUrl", user.getProfileImageUrl());
		model.addAttribute("nickname", user.getNickname());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("level", user.getLevel());
		model.addAttribute("selfIntro", user.getSelfIntro());
		model.addAttribute("snsLink", user.getSnsLink());
		addUserDetailsToModel(model, userDetails);
		addMenuData(model, userDetails);
		model.addAttribute("mainFragment1", "fragment/edit-profile");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/mypage/edit-objectives")
	public String objectivesPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getUserId();

		addUserDetailsToModel(model, userDetails);

		// DB에서 유저의 목표 데이터를 가져오기
		List<Objective> objectives = myPageService.getObjectives(userId);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		model.addAttribute("userId", userId);
		model.addAttribute("objectives", objectives);
		model.addAttribute("mainFragment1", "fragment/edit-objective");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index"; // HTML 파일 이름
	}

	@GetMapping("/category/search")
	public String searchCategoryPosts(@RequestParam(defaultValue = "0", name = "page") int page,
		@RequestParam(name = "searchType") String searchType,
		@RequestParam(name = "query") String keyword, @RequestParam(name = "id") Long categoryId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		Page<PostResponseDTO> searchResults;
		if ("title".equals(searchType)) {
			searchResults = postService.searchPostsByTitle(keyword, PostType.PARTICIPATION_CATEGORY.toString(),
				categoryId, page, 30);
		} else {
			searchResults = postService.searchPostsByTitleOrContent(keyword, PostType.PARTICIPATION_CATEGORY.toString(),
				categoryId, page, 30);
		}

		model.addAttribute("keyword", keyword);
		model.addAttribute("searchResults", searchResults);
		addCategoryPageData(categoryId, page, model, userDetails);
		addUserDetailsToModel(model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/region/search")
	public String searchRegionPosts(@RequestParam(defaultValue = "0", name = "page") int page,
		@RequestParam(name = "searchType") String searchType,
		@RequestParam(name = "query") String keyword, @RequestParam(name = "id") Long regionId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		Page<PostResponseDTO> searchResults;
		if ("title".equals(searchType)) {
			searchResults = postService.searchPostsByTitle(keyword, PostType.PARTICIPATION_REGION.toString(), regionId,
				page, 30);
		} else {
			searchResults = postService.searchPostsByTitleOrContent(keyword, PostType.PARTICIPATION_REGION.toString(),
				regionId, page, 30);
		}

		model.addAttribute("keyword", keyword);
		model.addAttribute("searchResults", searchResults);
		addRegionPageData(regionId, page, model, userDetails);
		addUserDetailsToModel(model, userDetails);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/challenge/search")
	public String searchChallengePosts(@RequestParam(defaultValue = "0", name = "page") int page,
		@RequestParam(name = "searchType") String searchType,
		@RequestParam(name = "query") String keyword, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.PARTICIPATION_CHALLENGE.getKoreanName();

		Page<PostResponseDTO> searchResults;
		if ("title".equals(searchType)) {
			searchResults = postService.searchPostsByTitle(keyword, PostType.PARTICIPATION_CHALLENGE.toString(), null,
				page, 30);
		} else {
			searchResults = postService.searchPostsByTitleOrContent(keyword, PostType.PARTICIPATION_CHALLENGE.toString(),
				null, page, 30);
		}

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		List<PostResponseDTO> todayBest = postService.getTodayBestChallengePost();
		List<PostResponseDTO> weeklyBest = postService.getWeeklyBestChallengePost();
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("keyword", keyword);
		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.PARTICIPATION_CHALLENGE.toString());
		model.addAttribute("todayBest", todayBest);
		model.addAttribute("weeklyBest", weeklyBest);
		model.addAttribute("postList", searchResults.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", searchResults.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/announcement/search")
	public String SearchAnnouncementPosts(@RequestParam(defaultValue = "0", name = "page") int page,
		@RequestParam(name = "searchType") String searchType,
		@RequestParam(name = "query") String keyword, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.ANNOUNCEMENT.getKoreanName();

		Page<PostResponseDTO> searchResults;
		if ("title".equals(searchType)) {
			searchResults = postService.searchPostsByTitle(keyword, PostType.ANNOUNCEMENT.toString(), null,
				page, 30);
		} else {
			searchResults = postService.searchPostsByTitleOrContent(keyword, PostType.ANNOUNCEMENT.toString(),
				null, page, 30);
		}

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("keyword", keyword);
		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.ANNOUNCEMENT.toString());
		model.addAttribute("postList", searchResults.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", searchResults.getTotalPages());
		model.addAttribute("isAdmin", userDetails.getUser().getLevel().equals("관리자"));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/bulletin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	@GetMapping("/admin")
	public String getPostList(
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "postType", required = false) String postType,
		@RequestParam(value = "targetId", required = false) Long targetId,
		@RequestParam(value = "isActive", required = false) Boolean isActive,
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "sort", defaultValue = "latest") String sort, // 기본값 최신순
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "30") int size,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Page<Map<String, Object>> paginatedPosts = adminService.getFilteredPosts(
			searchType, postType, targetId, isActive, query, sort, page, size
		);
		addUserDetailsToModel(model, userDetails);
		model.addAttribute("postList", paginatedPosts.getContent());
		model.addAttribute("totalPages", paginatedPosts.getTotalPages());
		model.addAttribute("currentPage", page);
		model.addAttribute("sort", sort); // 현재 정렬 상태 전달

		List<PostResponseDTO> todayLikedPosts = postService.getTodayTopLikedPosts();

		// 이달의 활동왕
		List<User> monthlyTopUsers = postService.getMonthlyTopUsers();

		// 모델에 데이터 추가
		model.addAttribute("todayLikedPosts", todayLikedPosts);
		model.addAttribute("monthlyTopUsers", monthlyTopUsers);

		//model.addAttribute("categoryName", "관리자 메뉴");

		//model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/admin-board-list");
		model.addAttribute("sideFragment1", "fragment/main-page-signin");
		model.addAttribute("sideFragment2", "fragment/main-page-best");
		return "index";
	}

	private List<PostResponseDTO> getSubList(List<PostResponseDTO> list, int start, int end) {
		if (list == null || list.isEmpty() || start >= list.size()) {
			return Collections.emptyList();
		}
		// 안전한 끝 인덱스 계산
		int toIndex = Math.min(end, list.size());
		return list.subList(start, toIndex);
	}

	private void addUserDetailsToModel(Model model, CustomUserDetails userDetails) {
		if (userDetails != null) {
			// 로그인된 사용자의 정보를 가져옵니다.
			User user = userRepository.findById(userDetails.getUser().getUserId())
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

			// 로그인된 사용자 정보를 모델에 추가
			model.addAttribute("isLoggedIn", true);
			model.addAttribute("userNickname", user.getNickname());
			model.addAttribute("userLevel", user.getLevel());
			model.addAttribute("userActivityScore", user.getActivityScore());
			model.addAttribute("profileImageUrl", user.getProfileImageUrl());
		} else {
			// 비로그인 상태일 경우
			model.addAttribute("isLoggedIn", false);
		}
	}

}
