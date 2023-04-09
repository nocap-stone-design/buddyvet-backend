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
import com.capstone.buddyvet.dto.Diary.AddRequest;
import com.capstone.buddyvet.dto.Diary.AddResponse;
import com.capstone.buddyvet.dto.Diary.DetailResponse;
import com.capstone.buddyvet.dto.Diary.DiariesResponse;
import com.capstone.buddyvet.dto.Diary.ImageRemoveRequest;
import com.capstone.buddyvet.service.DiaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
public class DiaryController {

	private final DiaryService diaryService;

	/**
	 * 일기 목록 조회
	 * @param year    조회할 년도
	 * @param month    조회할 월
	 * @return 해당 년월에 작성된 일기 목록
	 */
	@GetMapping
	public ResponseDto<DiariesResponse> diaryList(@RequestParam String year, @RequestParam String month) {
		return new ResponseDto<>(diaryService.getDiaries(year, month));
	}

	/**
	 * 일기 상세 조회
	 * @param diaryId 조회활 일기 ID
	 * @return ID 에 해당하는 일기 상세 정보
	 */
	@GetMapping("/{diaryId}")
	public ResponseDto<DetailResponse> diaryDetails(@PathVariable Long diaryId) {
		return new ResponseDto<>(diaryService.getDiary(diaryId));
	}

	/**
	 * 일기 신규 작성
	 * @param request 신규 작성할 일기 정보
	 * @return 신규 생성된 일기 ID
	 */
	@PostMapping
	public ResponseDto<AddResponse> diaryAdd(@RequestBody AddRequest request) {
		return new ResponseDto<>(diaryService.addDiary(request));
	}

	/**
	 * 일기 수정
	 * @param diaryId 수정할 일기 ID
	 * @param request 수정할 일기 정보
	 * @return null
	 */
	@PutMapping("/{diaryId}")
	public ResponseDto diaryModify(@PathVariable Long diaryId, @RequestBody AddRequest request) {
		diaryService.modifyDiary(diaryId, request);
		return new ResponseDto(null);

	}

	/**
	 * 일기 삭제
	 * @param diaryId 삭제할 일기 ID
	 * @return null
	 */
	@DeleteMapping("/{diaryId}")
	public ResponseDto diaryRemove(@PathVariable Long diaryId) {
		diaryService.removeDiary(diaryId);
		return new ResponseDto(null);
	}

	/**
	 * 일기 사진 업로드
	 * @param diaryId 사진 추가할 일기 ID
	 * @param files 업로드할 사진 리스트
	 * @return null
	 */
	@PostMapping("/{diaryId}/image")
	public ResponseDto imageUpload(@PathVariable Long diaryId, @RequestParam("image") List<MultipartFile> files) {
		diaryService.uploadImage(diaryId, files);
		return new ResponseDto(null);
	}

	/**
	 * 일기 사진 삭제
	 * @param diaryId 사진이 속해있는 일기 ID
	 * @param request 삭제할 사진 ID
	 * @return null
	 */
	@DeleteMapping("/{diaryId}/image")
	public ResponseDto imageRemove(@PathVariable Long diaryId, @RequestBody ImageRemoveRequest request) {
		diaryService.removeImages(diaryId, request.getImageIds());
		return new ResponseDto(null);
	}
}
