package com.capstone.buddyvet.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.capstone.buddyvet.common.file.S3Uploader;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.UserDiary;
import com.capstone.buddyvet.domain.UserDiaryImage;
import com.capstone.buddyvet.domain.enums.DiaryState;
import com.capstone.buddyvet.domain.enums.ImageState;
import com.capstone.buddyvet.dto.Diary.AddRequest;
import com.capstone.buddyvet.dto.Diary.AddResponse;
import com.capstone.buddyvet.dto.Diary.DetailResponse;
import com.capstone.buddyvet.dto.Diary.DiariesResponse;
import com.capstone.buddyvet.repository.DiaryImageRepository;
import com.capstone.buddyvet.repository.DiaryRepository;
import com.capstone.buddyvet.repository.UserDiaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

	private final AuthService authService;
	private final DiaryRepository diaryRepository;
	private final UserDiaryRepository userDiaryRepository;
	private final DiaryImageRepository diaryImageRepository;
	private final S3Uploader s3Uploader;

	public DiariesResponse getDiaries(String year, String month) {
		try {
			LocalDate date = LocalDate.parse(year + "-" + month + "-01");

			YearMonth yearMonth = YearMonth.from(date);
			LocalDate startDate = yearMonth.atDay(1);
			LocalDate endDate = yearMonth.atEndOfMonth();

			User user = authService.getCurrentActiveUser();

			List<UserDiary> diaries = userDiaryRepository.findAllByUserAndStateAndDateBetween(user, DiaryState.ACTIVE,
				startDate, endDate);

			return new DiariesResponse(startDate, diaries);
		} catch (DateTimeParseException e) {
			throw new RestApiException(ErrorCode.INVALID_DATE_FORMAT);
		}
	}

	public DetailResponse getDiary(Long diaryId) {
		UserDiary diary = getDiaryAndValidate(diaryId);
		return DetailResponse.of(diary);
	}

	@Transactional
	public AddResponse addDiary(AddRequest request) {
		UserDiary savedDiary = diaryRepository.save(request.toEntity(authService.getCurrentActiveUser()));
		return new AddResponse(savedDiary.getId());
	}

	@Transactional
	public void modifyDiary(Long diaryId, AddRequest request) {
		UserDiary diary = getDiaryAndValidate(diaryId);
		diary.update(request);
	}

	@Transactional
	public void removeDiary(Long diaryId) {
		UserDiary diary = getDiaryAndValidate(diaryId);
		diary.delete();
	}

	@Transactional
	public void uploadImage(Long diaryId, List<MultipartFile> files) {
		UserDiary diary = getDiaryAndValidate(diaryId);

		User currentUser = authService.getCurrentActiveUser();
		List<String> urls = s3Uploader.uploadFiles(files, currentUser.getId().toString());

		for (String url : urls) {
			diary.saveImage(new UserDiaryImage(url));
		}
	}

	@Transactional
	public void removeImages(Long diaryId, List<Long> imageIds) {
		UserDiary diary = getDiaryAndValidate(diaryId);
		List<UserDiaryImage> diaryImages = diaryImageRepository.findAllByIdInAndState(imageIds, ImageState.ACTIVE);

		if (imageIds.size() != diaryImages.size()) {
			throw new RestApiException(ErrorCode.INVALID_IMAGE_IDS);
		}

		validateAndDeleteDiaryImage(diary, diaryImages);
	}

	/**
	 * 해당 일기장이 현재 로그인 유저의 일기인지 검증
	 * @param diary 검증할 일기
	 *             현재 유저의 id 와 일기를 작성한 유저의 id 가 일치하지 않으면 exception
	 */
	private void validateDiaryUser(UserDiary diary) {
		if (diary.getUser().getId() != authService.getCurrentActiveUser().getId()) {
			throw new RestApiException(ErrorCode.INVALID_ACCESS);
		}
	}

	/**
	 * 해당 사진이 파라미터로 전달된 일기글에 속해있는지 검증
	 * TODO fetch join 으로 쿼리 단에서 해결할 수 있는지 확인하기
	 * @param diary 검증할 일기
	 * @param diaryImages 검증할 일기 사진
	 *                  파라미터로 전송된 일기글 id 와 사진이 속해있는 일기글 id 가 일치하지 않으면 exception
	 */
	private void validateAndDeleteDiaryImage(UserDiary diary, List<UserDiaryImage> diaryImages) {
		for (UserDiaryImage diaryImage : diaryImages) {
			if (!diary.getId().equals(diaryImage.getUserDiary().getId())) {
				throw new RestApiException(ErrorCode.INVALID_ACCESS);
			}
			diaryImage.delete();
		}
	}

	/**
	 * 일기 ID 로 일기 엔티티 조회
	 * @param diaryId 조회할 일기 ID
	 * @return ID 에 해당하는 일기 엔티티
	 */
	private UserDiary getDiaryAndValidate(Long diaryId) {
		UserDiary diary = diaryRepository.findByIdAndState(diaryId, DiaryState.ACTIVE)
			.orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_DIARY));
		validateDiaryUser(diary);

		return diary;
	}
}
