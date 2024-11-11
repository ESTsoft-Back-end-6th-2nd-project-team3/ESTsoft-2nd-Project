package com.estsoft.estsoft2ndproject.controller.main;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.estsoft.estsoft2ndproject.domain.Objective;
import com.estsoft.estsoft2ndproject.domain.SubMenu;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.service.ObjectiveService;
import com.estsoft.estsoft2ndproject.service.UserService;

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
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.service.CommentService;
import com.estsoft.estsoft2ndproject.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	private final PostService postService;
	private final CommentService commentService;
	private final UserService userService;
	private final ObjectiveService objectiveService;

	public PageController(PostService postService, CommentService commentService, UserService userService,
		ObjectiveService objectiveService) {
		this.postService = postService;
		this.commentService = commentService;
		this.userService = userService;
		this.objectiveService = objectiveService;
	}

	@GetMapping("/")
	public String menuPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		addMenuData(model, userDetails);
		return "index";
	}

	private void addMenuData(Model model, CustomUserDetails userDetails) {
		String level = "";

		if (userDetails != null) {
			User user = userDetails.getUser();

			level = user.getLevel();
		}

		model.addAttribute("subMenus", postService.getSubMenus(level));
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
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
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
	public String categoryPage(@RequestParam(defaultValue = "0") int page, @RequestParam(name = "id") Long categoryId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addCategoryPageData(categoryId, page, model, userDetails);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");

		return "index";
	}

	@GetMapping("/region")
	public String regionPage(@RequestParam(defaultValue = "0") int page, @RequestParam(name = "id") Long regionId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addRegionPageData(regionId, page, model, userDetails);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");

		return "index";
	}

	@GetMapping("/challenge")
	public String challengePage(@RequestParam(defaultValue = "0") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.PARTICIPATION_CHALLENGE.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

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
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");
		return "index";
	}

	@GetMapping("/announcement")
	public String announcementPage(@RequestParam(defaultValue = "0") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.ANNOUNCEMENT.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostType(PostType.ANNOUNCEMENT.toString(),
			page, 30);

		model.addAttribute("categoryName", boardName);
		model.addAttribute("postType", PostType.ANNOUNCEMENT.toString());
		model.addAttribute("postList", postPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", postPage.getTotalPages());
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/bulletin-board-list");
		return "index";
	}

	@GetMapping("/write")
	public String writeCategoryPost(@RequestParam(required = false) Long postId,
		@RequestParam(name = "postType") String postType,
		@RequestParam(name = "targetId") Long targetId, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = postService.getCategoryByPostType(postType, targetId);

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		List<Category> categories = null;
		List<Region> regions = null;

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

		model.addAttribute("categoryName", boardName);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("categories", categories);
		model.addAttribute("regions", regions);
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/write-post");
		return "index";
	}

	@GetMapping("/writeChallenge")
	public String writeChallengePost(@RequestParam(required = false) Long postId,
		@RequestParam(name = "postType") String postType, Model model,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.getKoreanNameByString(postType);

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

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

		model.addAttribute("categoryName", boardName);
		model.addAttribute("userId", userDetails.getUser().getUserId());
		model.addAttribute("postTypes", filteredPostTypes);
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/write-post");
		return "index";
	}

	@GetMapping("/category/post/{postId}")
	public String categoryPostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(name = "targetId") Long categoryId,
		@RequestParam(defaultValue = "0") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addCategoryPageData(categoryId, page, model, userDetails);

		postService.increaseViewCount(postId);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);

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

		return "index";
	}

	@GetMapping("/region/post/{postId}")
	public String regionPostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(name = "targetId") Long regionId,
		@RequestParam(defaultValue = "0") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addRegionPageData(regionId, page, model, userDetails);

		postService.increaseViewCount(postId);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);

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

		return "index";
	}

	@GetMapping("/challenge/post/{postId}")
	public String challengePostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(defaultValue = "0") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.PARTICIPATION_CHALLENGE.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);
		List<PostResponseDTO> todayBest = postService.getTodayBestChallengePost();
		List<PostResponseDTO> weeklyBest = postService.getWeeklyBestChallengePost();
		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostType(
			PostType.PARTICIPATION_CHALLENGE.toString(), page, 30);

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
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/view-post");
		model.addAttribute("mainFragment3", "fragment/view-comment");
		model.addAttribute("mainFragment4", "fragment/category-name");
		model.addAttribute("mainFragment5", "fragment/category-best");
		model.addAttribute("mainFragment6", "fragment/bulletin-board-list");

		return "index";
	}

	@GetMapping("/announcement/post/{postId}")
	public String announcementPostDetailPage(@PathVariable(name = "postId") Long postId,
		@RequestParam(defaultValue = "0") int page,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		String boardName = PostType.ANNOUNCEMENT.getKoreanName();

		addMenuData(model, userDetails);
		addCategoryNamePageData(model);

		PostResponseDTO post = postService.getPostDetail(postId);
		List<CommentResponseDTO> commentList = commentService.getCommentsDetailByPostId(postId);
		Page<PostResponseDTO> postPage = postService.getPaginationPostsByPostType(PostType.ANNOUNCEMENT.toString(),
			page, 30);

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
		model.addAttribute("isAdmin", postService.isAdmin(userDetails));
		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/view-post");
		model.addAttribute("mainFragment3", "fragment/view-comment");
		model.addAttribute("mainFragment4", "fragment/category-name");
		model.addAttribute("mainFragment5", "fragment/bulletin-board-list");

		return "index";
	}

	@GetMapping("/mypage")
	public String showMyPage(@SessionAttribute("userId") Long userId, Model model) {
		Optional<User> user = userService.getUserWithChallenges(userId);
		if (user.isPresent()) {
			User currentUser = user.get();
			model.addAttribute("nickname", currentUser.getNickname());
			model.addAttribute("level", currentUser.getLevel());
			model.addAttribute("selfIntro", currentUser.getSelfIntro());
			model.addAttribute("snsLink", currentUser.getSnsLink());
			model.addAttribute("participatedChallenge", currentUser.getAwardedTitle());

			model.addAttribute("subMenus", getSubMenus(currentUser.getLevel()));
		} else {
			model.addAttribute("nickname", "알 수 없음");
			model.addAttribute("level", "등급 없음");
			model.addAttribute("selfIntro", "소개 없음");
			model.addAttribute("snsLink", "링크 없음");
			model.addAttribute("participatedChallenge", "참여한 챌린지가 없습니다.");

			model.addAttribute("subMenus", getSubMenus("일반"));
		}

		int month = LocalDate.now().getMonthValue();
		model.addAttribute("month", month);

		List<Objective> myObjective = objectiveService.getObjectivesForUserAndMonth(userId,
			LocalDate.now().withDayOfMonth(1));

		int completedCount = (int)myObjective.stream().filter(Objective::getIsCompleted).count();
		int totalCount = myObjective.size();
		int progressValue = totalCount > 0 ? (completedCount * 100) / totalCount : 0;

		model.addAttribute("progressValue", progressValue);

		model.addAttribute("mainFragment1", "fragment/mypage-profile.html :: mypage-profile");
		model.addAttribute("mainFragment2", "fragment/my-objective.html :: my-objective");
		model.addAttribute("mainFragment3", "fragment/participated-challenge.html :: participated-challenge");

		return "index";
	}

	@GetMapping("/edit-profile")
	public String showEditProfile(Model model, @SessionAttribute(value = "userId", required = false) Long userId) {
		Optional<User> user = (userId != null) ? userService.getUserById(userId) : Optional.empty();

		model.addAttribute("user", user.orElse(new User()));

		model.addAttribute("subMenus", getSubMenus("일반"));
		model.addAttribute("mainFragment5", "fragment/edit-profile.html :: edit-profile");

		return "index";
	}

	@PostMapping("/mypage/{userId}/userinfo")
	public ResponseEntity<Map<String, Object>> updateProfile(
		@PathVariable Long userId,
		@RequestParam("nickname") String nickname,
		@RequestParam("selfIntro") String selfIntro,
		@RequestParam("snsLink") String snsLink,
		@RequestParam("profileImage") MultipartFile profileImage) {

		// 파일을 저장할 경로 설정
		String filePath = "uploads/" + profileImage.getOriginalFilename();
		File file = new File(filePath);
		try {
			profileImage.transferTo(file); // 파일 저장
		} catch (IOException e) {
			e.printStackTrace();
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "이미지 업로드에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}

		// 사용자 정보 업데이트 로직
		userService.updateUserProfile(userId, nickname, selfIntro, snsLink, filePath);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		return ResponseEntity.ok(response);
	}

	private List<SubMenu> getSubMenus(String level) {
		if ("관리자".equals(level)) {
			return Arrays.asList(
				new SubMenu("카테고리", Arrays.asList("음악", "사진", "여행", "게임"), null, null),
				new SubMenu("챌린지", Arrays.asList("챌린지 아이템", "특별 챌린지"), null, null),
				new SubMenu("지역 친목 게시판", Arrays.asList(
					"강원특별자치도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시",
					"대전광역시", "부산광역시", "서울특별시", "세종특별자치시", "울산광역시",
					"인천광역시", "전라남도", "전북특별자치도", "제주특별자치도", "충청남도", "충청북도"
				), null, null),
				new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기"), null, null),
				new SubMenu("관리자 메뉴", Arrays.asList("사용자 관리", "사이트 설정"), null, null)
			);
		} else {
			return Arrays.asList(
				new SubMenu("카테고리", Arrays.asList("취미1", "취미2"), null, null),
				new SubMenu("챌린지", Arrays.asList("챌린지 아이템 1"), null, null),
				new SubMenu("지역 친목 게시판", Arrays.asList("강원도", "경기도", "서울특별시"), null, null),
				new SubMenu("마이페이지", Arrays.asList("프로필 설정", "내 활동 보기"), null, null)
			);
		}
	}
}



