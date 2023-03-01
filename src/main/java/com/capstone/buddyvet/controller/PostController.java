package com.capstone.buddyvet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.buddyvet.common.dto.ResponseDto;
import com.capstone.buddyvet.dto.CommunityPost.AddRequest;
import com.capstone.buddyvet.dto.CommunityPost.AddResponse;
import com.capstone.buddyvet.dto.CommunityPost.ImageRemoveRequest;
import com.capstone.buddyvet.dto.Example.PostDetailResponse;
import com.capstone.buddyvet.dto.Example.PostsResponse;
import com.capstone.buddyvet.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
public class PostController {

	private final PostService postService;

	/**
	 * 게시글 목록 조회
	 * @return 작성된 게시글 목록
	 * TODO Ex 에서 실제 구현으로 변경
	 */
	@GetMapping
	public ResponseDto<PostsResponse> postListEx() {
		return new ResponseDto<>(postService.getPosts());
	}

	/**
	 * 게시글 상세 조회
	 * @param postId 조회할 게시글 ID
	 * @return ID 에 해당하는 게시글 상세 정보
	 * TODO Ex 에서 실제 구현으로 변경
	 */
	@GetMapping("/{postId}")
	public ResponseDto<PostDetailResponse> postDetailsEx(@PathVariable Long postId) {
		return new ResponseDto<>(postService.getPost(postId));
	}

	/**
	 * 게시글 신규 작성
	 * @param request 신규 작성할 게시글 정보
	 * @return 신규 생성된 게시글 ID
	 */
	@PostMapping
	public ResponseDto<AddResponse> postAdd(@RequestBody AddRequest request) {
		return new ResponseDto<>(postService.addPost(request));
	}

	/**
	 * 게시글 수정
	 * @param postId 수정할 게시글 ID
	 * @param request 수정할 게시글 정보
	 * @return null
	 */
	@PutMapping("/{postId}")
	public ResponseDto postModify(@PathVariable Long postId, @RequestBody AddRequest request) {
		postService.modifyPost(postId, request);
		return new ResponseDto(null);
	}

	/**
	 * 게시글 삭제
	 * @param postId 삭제할 게시글 ID
	 * @return null
	 */
	@DeleteMapping("/{postId}")
	public ResponseDto postRemove(@PathVariable Long postId) {
		postService.removePost(postId);
		return new ResponseDto(null);
	}

	/**
	 * 게시글 사진 업로드
	 * @param postId 사진 추가할 게시글 ID
	 * @param files 업로드할 사진 리스트
	 * @return null
	 */
	@PostMapping("/{postId}/image")
	public ResponseDto imageUpload(@PathVariable Long postId, @RequestParam("image") List<MultipartFile> files) {
		log.info(files.get(0).getOriginalFilename());
		postService.uploadImage(postId, files);
		return new ResponseDto(null);
	}

	/**
	 * 게시글 사진 삭제
	 * @param postId 사진이 속해있는 게시글 ID
	 * @param request 삭제할 사진 ID
	 * @return null
	 */
	@DeleteMapping("/{postId}/image")
	public ResponseDto imageRemove(@PathVariable Long postId, @RequestBody ImageRemoveRequest request) {
		postService.removeImage(postId, request.getImageId());
		return new ResponseDto(null);
	}
}
