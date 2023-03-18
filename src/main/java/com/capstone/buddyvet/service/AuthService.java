package com.capstone.buddyvet.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.buddyvet.common.auth.KakaoAuthService;
import com.capstone.buddyvet.common.dto.ResponseDto;
import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.UserState;
import com.capstone.buddyvet.dto.Auth.JwtResponse;
import com.capstone.buddyvet.dto.Auth.LoginRequest;
import com.capstone.buddyvet.dto.Auth.SocialUserInfo;
import com.capstone.buddyvet.repository.UserRepository;
import com.capstone.buddyvet.security.config.Properties;
import com.capstone.buddyvet.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final KakaoAuthService kakaoAuthService;
	private final JwtTokenProvider tokenProvider;
	private final Properties properties;

	/**
	 * 로그인/회원가입
	 * @param request
	 * @return jwt
	 */
	@Transactional
	public ResponseEntity<ResponseDto<JwtResponse>> login(LoginRequest request) {
		SocialUserInfo userInfo = kakaoAuthService.getUserInfo(request)
			.orElseThrow(() -> new RestApiException(ErrorCode.OAUTH_PROVIDER_CONNECT_ERROR));

		Optional<User> findUser = userRepository.findUserBySocialId(userInfo.getProviderType(),
			userInfo.getLoginId(), UserState.ACTIVE);

		User user = findUser.orElseGet(() -> userService.registerUser(userInfo));
		String jwt = tokenProvider.create(user.getSocialId(), user.getState(), properties.getSecret());

		if (findUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto<>(new JwtResponse(jwt)));
		}
		return ResponseEntity.ok(new ResponseDto<>(new JwtResponse(jwt)));
	}

	/**
	 * 현재 로그인 유저 반환
	 * @return 현재 jwt 로 SecurityContext 에 저장된 social id 를 이용해 유저 엔티티 조회 후 반환
	 * 없을 시 LOAD_USER_ERROR exception
	 */
	public User getCurrentActiveUser() {
		User user = userRepository.findBySocialId(tokenProvider.getUsername())
			.orElseThrow(() -> new RestApiException(ErrorCode.LOAD_USER_ERROR));

		if (user.getState() != UserState.ACTIVE) {
			throw new RestApiException(ErrorCode.NOT_ACTIVATED_USER);
		}

		return user;
	}
}
