package com.estsoft.estsoft2ndproject.domain.dto.comment;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentCommentResponseDTO {
	private Long commentId;
	private String content;
	private Long postId;
	private Long userId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp updatedAt;
	private Integer likeCount;
}
