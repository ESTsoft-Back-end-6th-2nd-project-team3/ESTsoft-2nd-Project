package com.estsoft.estsoft2ndproject.domain.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDTO {
	private Long postId;
	private String title;
	private String content;
	private Long userId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String postType;
	private Long targetId;
	private Boolean isActive;
	private Integer viewCount;
	private Integer likeCount;
	private Boolean isLiked;
	private Integer commentCount;
	private String postLink; // 게시글 링크 추가

	public CommentResponseDTO(PostResponseDTO postResponseDTO, int commentCount) {
		this.postId = postResponseDTO.getPostId();
		this.title = postResponseDTO.getTitle();
		this.content = postResponseDTO.getContent();
		this.userId = postResponseDTO.getUserId();
		this.createdAt = postResponseDTO.getCreatedAt();
		this.updatedAt = postResponseDTO.getUpdatedAt();
		this.postType = postResponseDTO.getPostType();
		this.targetId = postResponseDTO.getTargetId();
		this.isActive = postResponseDTO.getIsActive();
		this.viewCount = postResponseDTO.getViewCount();
		this.likeCount = postResponseDTO.getLikeCount();
		this.isLiked = postResponseDTO.getIsLiked();
		this.commentCount = commentCount;
		this.postLink = "/post/" + postResponseDTO.getPostId(); // 링크 생성
	}
}

