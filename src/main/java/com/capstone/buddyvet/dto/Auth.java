package com.capstone.buddyvet.dto;

import com.capstone.buddyvet.domain.enums.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Auth {

	/**
	 * 로그인/회원가입 Request
	 */
	@Getter
	public static class LoginRequest {
		private String accessToken;
		private Provider providerType;
	}

	/**
	 * 소셜 Provider 에서 받아온 유저 정보 DTO
	 */
	@Getter
	@AllArgsConstructor
	@Builder
	public static class SocialUserInfo {
		private String loginId;
		private Provider providerType;
		private String nickname;
	}

	/**
	 * 로그인/회원가입 jwt Response
	 */
	@Getter
	@AllArgsConstructor
	public static class JwtResponse {
		private String jwt;
	}
}
