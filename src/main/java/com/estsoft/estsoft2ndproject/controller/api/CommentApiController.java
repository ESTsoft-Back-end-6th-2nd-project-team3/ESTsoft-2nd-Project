package com.estsoft.estsoft2ndproject.controller.api;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.estsoft.estsoft2ndproject.domain.Comment;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentListResponseDTO;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentRequestDTO;
import com.estsoft.estsoft2ndproject.domain.dto.comment.CommentResponseDTO;
import com.estsoft.estsoft2ndproject.service.CommentService;
import com.estsoft.estsoft2ndproject.service.UserService;

@Controller
@RequestMapping("/api/post")
public class CommentApiController {
	private static final Logger log = LoggerFactory.getLogger(CommentApiController.class);
	private final CommentService commentService;
	private final UserService userService;

	public CommentApiController(CommentService commentService, UserService userService) {
		this.commentService = commentService;
		this.userService = userService;
	}

	@GetMapping("/{postId}/comment")
	public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable(name = "postId") Long postId) {
		List<CommentResponseDTO> commentList = commentService.getCommentsByPostId(postId)
			.stream()
			.map(CommentResponseDTO::new)
			.toList();
		return ResponseEntity.ok(commentList);
	}

	@PostMapping("/{postId}/comment")
	public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO,
		@PathVariable(name = "postId") Long postId,
		@AuthenticationPrincipal OAuth2User oAuth2User) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(commentService.createComment(commentRequestDTO, postId, oAuth2User).convert());
	}

	@PostMapping("/{postId}/comment/{commentId}")
	public ResponseEntity<CommentResponseDTO> createCommentWithParent(@RequestBody CommentRequestDTO commentRequestDTO,
		@PathVariable(name = "postId") Long postId,
		@PathVariable(name = "commentId") Long commentId,
		@AuthenticationPrincipal OAuth2User oAuth2User) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(commentService.createComment(commentRequestDTO, postId, commentId, oAuth2User).convert());
	}

	@PutMapping("/comment/{commentId}")
	public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
		log.error("commentId: {}", commentId);
		log.error("commentRequestDTO: {}", commentRequestDTO);
		Comment comment = commentService.updateComment(commentId, commentRequestDTO);
		return ResponseEntity.ok(new CommentResponseDTO(comment));
	}

	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable(name = "commentId") Long commentId) {
		commentService.deleteComment(commentId);
		return ResponseEntity.ok().build();
	}

	// TODO 좋아요 API 구현
	/*@PostMapping("/comment/{commentId}/like")
	public String likeComment() {
		return "comment";
	}*/

	@GetMapping("/comment")
	public String commentTest() {
		return "testHtml/comment-test";
	}

	@GetMapping("/{postId}/comments")
	public String commentsTest(Model model, @PathVariable(name = "postId") Long postId, @AuthenticationPrincipal OAuth2User oAuth2User) {
		List<CommentListResponseDTO> commentList = commentService.getCommentsByPostId(postId)
			.stream()
			.map(CommentListResponseDTO::new)
			.sorted(Comparator.comparing(CommentListResponseDTO::getCreatedAt))
			.toList();

		Long userId = userService.getUserId(oAuth2User);

		model.addAttribute("comments", commentList);
		model.addAttribute("userId", userId);
		model.addAttribute("commentCount", commentService.getCommentCountByPostId(postId));

		return "fragment/view-comment";
	}
}
