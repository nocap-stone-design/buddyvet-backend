package com.capstone.buddyvet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.capstone.buddyvet.common.file.S3Uploader;
import com.capstone.buddyvet.domain.Post;
import com.capstone.buddyvet.domain.PostImage;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.ImageState;
import com.capstone.buddyvet.domain.enums.PostState;
import com.capstone.buddyvet.dto.CommunityPost.AddRequest;
import com.capstone.buddyvet.dto.CommunityPost.AddResponse;
import com.capstone.buddyvet.dto.Example.PostDetailResponse;
import com.capstone.buddyvet.dto.Example.PostsResponse;
import com.capstone.buddyvet.repository.PostImageRepository;
import com.capstone.buddyvet.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
	private final AuthService authService;
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final S3Uploader s3Uploader;

	public PostsResponse getPosts() {
		return new PostsResponse(postRepository.findAll());
	}

	public PostDetailResponse getPost(Long postId) {
		Post post = getPostAndValidate(postId);
		return PostDetailResponse.of(post);
	}

	@Transactional
	public AddResponse addPost(AddRequest request) {
		Post savedPost = postRepository.save(request.toEntity(authService.getCurrentUser()));
		return new AddResponse(savedPost.getId());
	}

	@Transactional
	public void modifyPost(Long postId, AddRequest request) {
		Post post = getPostAndValidate(postId);
		post.update(request);
	}

	@Transactional
	public void removePost(Long postId) {
		Post post = getPostAndValidate(postId);
		post.delete();
	}

	@Transactional
	public void uploadImage(Long postId, List<MultipartFile> files) {
		Post post = getPostAndValidate(postId);

		User currentUser = authService.getCurrentUser();
		List<String> urls = s3Uploader.uploadFiles(files, currentUser.getId().toString());

		for (String url : urls) {
			post.saveImage(new PostImage(url));
		}
	}

	@Transactional
	public void removeImage(Long postId, Long imageId) {
		Post post = getPostAndValidate(postId);
		PostImage postImage = postImageRepository.findByIdAndState(imageId, ImageState.ACTIVE)
			.orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_POST_IMAGE));

		validatePostImageUser(post, postImage);
		postImage.delete();
	}

	/**
	 * 해당 게시글이 현재 로그인 유저의 게시글인지 검증
	 * @param post 검증할 게시글
	 *             현재 유저의 id 와 게시글을 작성한 유저의 id 가 일치하지 않으면 exception
	 */
	private void validatePostUser(Post post) {
		if (post.getUser().getId() != authService.getCurrentUser().getId()) {
			throw new RestApiException(ErrorCode.INVALID_ACCESS);
		}
	}

	private void validatePostImageUser(Post post, PostImage postImage) {
		if (postImage.getPost().getId() != post.getId()) {
			throw new RestApiException(ErrorCode.INVALID_ACCESS);
		}
	}

	/**
	 * 게시글 ID 로 게시글 엔티티 조회
	 * @param postId 조회할 게시글 ID
	 * @return ID 에 해당하는 게시글 엔티티
	 */
	private Post getPostAndValidate(Long postId) {
		Post post = postRepository.findByIdAndState(postId, PostState.ACTIVE)
			.orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_POST));
		validatePostUser(post);

		return post;
	}
}