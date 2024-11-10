package com.estsoft.estsoft2ndproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.estsoft.estsoft2ndproject.domain.Category;
import com.estsoft.estsoft2ndproject.domain.Likes;
import com.estsoft.estsoft2ndproject.domain.Post;
import com.estsoft.estsoft2ndproject.domain.PostType;
import com.estsoft.estsoft2ndproject.domain.Region;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.domain.dto.post.PostRequestDTO;
import com.estsoft.estsoft2ndproject.exception.PostNotFoundException;
import com.estsoft.estsoft2ndproject.exception.UserNotFoundException;
import com.estsoft.estsoft2ndproject.repository.CategoryRepository;
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

	public PostService(PostRepository postRepository, UserRepository userRepository,
		CategoryRepository categoryRepository, RegionRepository regionRepository, LikesRepository likesRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.regionRepository = regionRepository;
		this.likesRepository = likesRepository;
	}

	@Transactional
	public Post createPost(PostRequestDTO postRequestDTO, Long userId) {
		// 추후에 User 구현 완료되면 로그인한 사용자 Id 가져와서 사용
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		return postRepository.save(postRequestDTO.toEntity(user));
	}

	@Transactional
	public Post getPostById(Long postId) {
		return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
	}

	public Post updatePost(Long postId, PostRequestDTO postRequestDTO) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
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

	public void addViewCount(Long postId) {
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
}
