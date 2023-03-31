package com.capstone.buddyvet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.capstone.buddyvet.common.file.S3Uploader;
import com.capstone.buddyvet.domain.Buddy;
import com.capstone.buddyvet.domain.BuddyBreed;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.dto.Buddies.BuddiesResponse;
import com.capstone.buddyvet.dto.Buddies.DetailResponse;
import com.capstone.buddyvet.dto.Buddies.SaveRequest;
import com.capstone.buddyvet.dto.Buddies.SaveResponse;
import com.capstone.buddyvet.repository.BuddyBreedRepository;
import com.capstone.buddyvet.repository.BuddyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BuddyService {

	private final AuthService authService;
	private final BuddyRepository buddyRepository;
	private final BuddyBreedRepository buddyBreedRepository;
	private final S3Uploader s3Uploader;

	public BuddiesResponse getBuddies() {
		User user = authService.getCurrentActiveUser();
		List<Buddy> buddies = buddyRepository.findAllByUserAndActivated(user, true);

		return new BuddiesResponse(buddies);
	}

	@Transactional
	public SaveResponse addBuddy(SaveRequest request) {
		User user = authService.getCurrentActiveUser();
		BuddyBreed buddyBreed = buddyBreedRepository.findById(request.getBreedId())
			.orElseThrow(() -> new RestApiException(ErrorCode.INVALID_BREED));
		Buddy buddy = buddyRepository.save(request.toEntity(user, buddyBreed));

		return new SaveResponse(buddy.getId());
	}

	public DetailResponse getBuddy(Long buddyId) {
		return DetailResponse.of(getUserBuddy(buddyId));
	}

	@Transactional
	public SaveResponse modifyBuddy(Long buddyId, SaveRequest request) {
		// TODO
		return new SaveResponse(buddyId);
	}

	@Transactional
	public void removeBuddy(Long buddyId) {
		getUserBuddy(buddyId).delete();
	}

	public void uploadImage(Long buddyId, MultipartFile file) {
		Buddy buddy = getUserBuddy(buddyId);
		User user = authService.getCurrentActiveUser();
		String url = s3Uploader.uploadSingleFile(file, user.getId().toString());
		buddy.saveImage(url);
	}

	private Buddy getUserBuddy(Long buddyId) {
		User user = authService.getCurrentActiveUser();
		Buddy buddy = buddyRepository.findByIdAndUserAndActivated(buddyId, user, true)
			.orElseThrow(() -> new RestApiException(ErrorCode.INVALID_BUDDY));
		return buddy;
	}
}
