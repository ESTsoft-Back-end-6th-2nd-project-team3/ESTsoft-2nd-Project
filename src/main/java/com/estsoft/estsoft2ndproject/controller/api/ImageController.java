package com.estsoft.estsoft2ndproject.controller.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/image")
@Slf4j
public class ImageController {
	private final String uploadPath;

	ImageController(@Value("${file.upload-dir}") String uploadPath) {
		this.uploadPath = uploadPath;
	}

	@PostMapping("/upload")
	public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			// 파일 이름 생성 (중복 방지를 위해 UUID 사용)
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

			// 저장할 경로 생성
			Path filePath = Paths.get(uploadPath, fileName);

			// 파일 저장
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			// 이미지에 접근할 수 있는 URL 생성
			String imageUrl = uploadPath + fileName; // 실제 서버의 URL 구조에 맞게 수정

			Map<String, String> response = new HashMap<>();
			response.put("imageUrl", imageUrl);

			return ResponseEntity.ok(response);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/post")
	public String postImg(@RequestParam("content") String content) {
		log.error(content);
		return "redirect:/";
	}
}