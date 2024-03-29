package com.capstone.buddyvet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.dto.Auth;
import com.capstone.buddyvet.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

	private final UserRepository userRepository;
	/**
	 * 유저 회원가입
	 * @param userInfo 소셜 유저 정보
	 * @return 유저 저장 후 유저 엔티티 반환
	 */
	@Transactional
	public User registerUser(Auth.SocialUserInfo userInfo) {
		return userRepository.save(User.builder()
			.socialId(userInfo.getLoginId())
			.nickname(userInfo.getNickname())
			.build());
	}
}
