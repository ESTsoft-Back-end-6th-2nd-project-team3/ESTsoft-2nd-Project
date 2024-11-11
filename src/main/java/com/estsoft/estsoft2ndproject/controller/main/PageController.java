package com.estsoft.estsoft2ndproject.controller.main;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.estsoft2ndproject.domain.Category;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.PostType;
import com.estsoft.estsoft2ndproject.domain.Region;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.user.CustomUserDetails;
import com.estsoft.estsoft2ndproject.service.CommentService;
import com.estsoft.estsoft2ndproject.service.PostService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PageController {
	private final PostService postService;
	private final CommentService commentService;

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
	public String categoryPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(name = "id") Long categoryId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addCategoryPageData(categoryId, page, model, userDetails);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");

		return "index";
	}

	@GetMapping("/region")
	public String regionPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(name = "id") Long regionId,
		Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

		addRegionPageData(regionId, page, model, userDetails);

		model.addAttribute("mainFragment1", "fragment/category-name");
		model.addAttribute("mainFragment2", "fragment/category-best");
		model.addAttribute("mainFragment3", "fragment/bulletin-board-list");

		return "index";
	}

	@GetMapping("/challenge")
	public String challengePage(@RequestParam(defaultValue = "0", name = "page") int page,
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
	public String announcementPage(@RequestParam(defaultValue = "0", name = "page") int page,
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
	public String writeCategoryPost(@RequestParam(required = false, name = "postId") Long postId,
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
	public String writeChallengePost(@RequestParam(required = false, name = "postId") Long postId,
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
		@RequestParam(defaultValue = "0", name = "page") int page,
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
		@RequestParam(defaultValue = "0", name = "page") int page,
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
		@RequestParam(defaultValue = "0", name = "page") int page,
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
		@RequestParam(defaultValue = "0", name = "page") int page,
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
}
