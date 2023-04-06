package com.capstone.buddyvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.buddyvet.common.file.S3Uploader;
import com.capstone.buddyvet.domain.Buddy;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.dto.Auth.SocialUserInfo;
import com.capstone.buddyvet.dto.Users;
import com.capstone.buddyvet.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final AuthService authService;
	private final UserRepository userRepository;
	private final S3Uploader s3Uploader;

	/**
	 * 유저 회원가입
	 * @param userInfo 소셜 유저 정보
	 * @return 유저 저장 후 유저 엔티티 반환
	 */
	@Transactional
	public User registerUser(SocialUserInfo userInfo) {
		return userRepository.save(User.builder()
			.socialId(userInfo.getLoginId())
				.nickname(userInfo.getNickname())
			.build());
	}

	public Users.DetailResponse getUser() {
		User user = authService.getCurrentActiveUser();
		return new Users.DetailResponse(user);
	}

	@Transactional
	public void addUserNickname(Users.AddNicknameRequest request) {
		User user = authService.getCurrentActiveUser();
		user.setNickname(request.getNickname());
	}

	@Transactional
	public void uploadImage(MultipartFile file) {
		User user = authService.getCurrentActiveUser();
		String url = s3Uploader.uploadSingleFile(file, user.getId().toString());
		user.setProfileImageUrl(url);
	}
}
