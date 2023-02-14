package com.capstone.buddyvet.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.buddyvet.common.dto.ResponseDto;
import com.capstone.buddyvet.dto.Buddy.SaveRequest;
import com.capstone.buddyvet.dto.Buddy.SaveResponse;
import com.capstone.buddyvet.dto.Buddy.BuddiesResponse;
import com.capstone.buddyvet.dto.Buddy.DetailResponse;
import com.capstone.buddyvet.service.BuddyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buddies")
public class BuddyController {

	private final BuddyService buddyService;

	/**
	 * 버디 목록 조회
	 * @return 등록된 버디 정보 목록
	 */
	@GetMapping
	public ResponseDto<BuddiesResponse> buddyList() {
		return new ResponseDto<>(buddyService.getBuddies());
	}

	/**
	 * 버디 신규 등록
	 * @param request 신규 등록할 버디 정보
	 * @return 신규 생성된 버디 ID
	 */
	@PostMapping
	public ResponseDto<SaveResponse> buddyAdd(@RequestBody SaveRequest request) {
		return new ResponseDto<>(buddyService.addBuddy(request));
	}

	/**
	 * 버디 상세 조회
	 * @param buddyId 조회할 버디 ID
	 * @return ID 에 해당하는 버디의 상세 정보
	 */
	@GetMapping("/{buddyId}")
	public ResponseDto<DetailResponse> buddyDetails(@PathVariable Long buddyId) {
		return new ResponseDto<>(buddyService.getBuddy(buddyId));
	}

	/**
	 * 버디 정보 수정
	 * @param buddyId 수정할 버디 ID
	 * @param request 수정할 버디 정보
	 * @return 수정된 버디 ID
	 */
	@PatchMapping("/{buddyId}")
	public ResponseDto<SaveResponse> buddyModify(@PathVariable Long buddyId, @RequestBody SaveRequest request) {
		return new ResponseDto<>(buddyService.modifyBuddy(buddyId, request));
	}

	/**
	 * 버디 삭제
	 * @param buddyId 삭제할 버디 ID
	 * @return 성공 시 null
	 */
	@DeleteMapping("/{buddyId}")
	public ResponseDto buddyRemove(@PathVariable Long buddyId) {
		return new ResponseDto(buddyService.removeBuddy(buddyId));
	}

	/**
	 * 버디 프로필 사진 업로드
	 * @param buddyId 사진 적용할 버디 ID
	 * @param file 적용할 이미지 파일
	 * @return 성공 시 null
	 */
	@PostMapping("/{buddyId}/image")
	public ResponseDto imageUpload(@PathVariable Long buddyId, @RequestParam("file") MultipartFile file) {
		return new ResponseDto(null);
	}
}
