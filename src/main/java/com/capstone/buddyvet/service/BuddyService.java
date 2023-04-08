package com.capstone.buddyvet.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.capstone.buddyvet.common.file.S3Uploader;
import com.capstone.buddyvet.domain.Buddy;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.Kind;
import com.capstone.buddyvet.dto.Buddies;
import com.capstone.buddyvet.dto.Buddies.BuddiesResponse;
import com.capstone.buddyvet.dto.Buddies.DetailResponse;
import com.capstone.buddyvet.dto.Buddies.SaveRequest;
import com.capstone.buddyvet.dto.Buddies.SaveResponse;
import com.capstone.buddyvet.repository.BuddyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BuddyService {

	private final AuthService authService;
	private final BuddyRepository buddyRepository;
	private final S3Uploader s3Uploader;
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;
	@Value("${model.dog}")
	private String dogUrl;
	@Value("${model.cat}")
	private String catUrl;

	public BuddiesResponse getBuddies() {
		User user = authService.getCurrentActiveUser();
		List<Buddy> buddies = buddyRepository.findAllByUserAndActivated(user, true);

		return new BuddiesResponse(buddies);
	}

	@Transactional
	public SaveResponse addBuddy(SaveRequest request) {
		User user = authService.getCurrentActiveUser();
		Buddy buddy = buddyRepository.save(request.toEntity(user));

		return new SaveResponse(buddy.getId());
	}

	public DetailResponse getBuddy(Long buddyId) {
		return DetailResponse.of(getUserBuddy(buddyId));
	}

	@Transactional
	public SaveResponse modifyBuddy(Long buddyId, SaveRequest request) {
		User user = authService.getCurrentActiveUser();
		Buddy buddy = buddyRepository.findByIdAndUserAndActivated(buddyId, user, true)
			.orElseThrow(() -> new RestApiException(ErrorCode.INVALID_BUDDY));
		buddy.modify(request);
		return new SaveResponse(buddyId);
	}

	@Transactional
	public void removeBuddy(Long buddyId) {
		getUserBuddy(buddyId).delete();
	}

	@Transactional
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

	public Buddies.EyeCheckResponse checkBuddyEye(Long buddyId, MultipartFile file) {
		Buddy buddy = getUserBuddy(buddyId);
		User user = authService.getCurrentActiveUser();
		String url = s3Uploader.uploadSingleFileForCheck(file, user.getId().toString());
		// TODO Serving server 로 보내서 결과받기
		Buddies.EyeCheckResult result = sendEyeImageFile(buddy, file);
		// TODO url 저장하기
		return new Buddies.EyeCheckResponse();
	}

	private Buddies.EyeCheckResult sendEyeImageFile(Buddy buddy, MultipartFile file) {
		try {
			MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
			bodyMap.add("file", new InputStreamResource(file.getInputStream()));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
			ResponseEntity<String> responseEntity = null;

			if (buddy.getKind() == Kind.D) {
				responseEntity = restTemplate.exchange(dogUrl, HttpMethod.POST, requestEntity, String.class);
			} else if (buddy.getKind() == Kind.C) {
				responseEntity = restTemplate.exchange(catUrl, HttpMethod.POST, requestEntity, String.class);
			} else {
				throw new RestApiException(ErrorCode.INVALID_BUDDY_KIND);
			}

			return objectMapper.readValue(responseEntity.getBody(), Buddies.EyeCheckResult.class);

		} catch (IOException e) {
			throw new RestApiException(ErrorCode.ML_NETWORK_ERROR);
		}
	}
}
