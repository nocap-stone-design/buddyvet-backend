package com.capstone.buddyvet.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.capstone.buddyvet.security.SecurityUtil;
import com.capstone.buddyvet.security.TokenProvider;

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
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
		String jwt = createJwt(user.getId().toString());

		if (findUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto<>(new JwtResponse(jwt)));
		}
		return ResponseEntity.ok(new ResponseDto<>(new JwtResponse(jwt)));
	}

	/**
	 * 유저 ID 에 해당하는 jwt 생성
	 * @param userId
	 * @return jwt
	 */
	private String createJwt(String userId) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			userId, "password");

		// loadUserByUsername 실행
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return tokenProvider.createToken(authentication);
	}

	/**
	 * 현재 로그인 유저 반환
	 * @return 현재 jwt 로 SecurityContext 에 저장된 id 를 이용해 유저 엔티티 조회 후 반환
	 * 없을 시 LOAD_USER_ERROR exception
	 */
	public User getCurrentUser() {
		return SecurityUtil.getCurrentUsername()
			.flatMap(id -> userRepository.findById(Long.valueOf(id)))
			.orElseThrow(() -> new RestApiException(ErrorCode.LOAD_USER_ERROR));
	}
}
