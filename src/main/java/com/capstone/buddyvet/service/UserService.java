package com.capstone.buddyvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
