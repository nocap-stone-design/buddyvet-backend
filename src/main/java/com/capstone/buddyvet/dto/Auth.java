package com.capstone.buddyvet.dto;

import com.capstone.buddyvet.domain.enums.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Auth {

	@Getter
	public static class LoginRequest {
		private String accessToken;
		private Provider providerType;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class LoginResponse {
		private String loginId;
		private Provider providerType;
		private String nickname;
		private String profileImage;
	}

	@Getter
	public static class JwtResponse {
		private String jwt;
	}
}
