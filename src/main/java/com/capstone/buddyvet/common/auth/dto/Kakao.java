package com.capstone.buddyvet.common.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Todo 요청을 받는 Nested Class에 대해 카멜 케이스로 변경할 것인지 확인하기.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kakao {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class User extends Auth.Info {
		private KakaoAccount kakao_account;

		public User(String id, KakaoAccount account) {
			super(id);
			this.kakao_account = account;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class KakaoAccount {
		private Profile profile;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Profile {
		private String nickname;
		private String thumbnail_image_url;
		private String profile_image_url;
		private String is_default_image;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class Token extends Auth.Token {
		private String tokenType;
		private Long expiresIn;
		private Long refreshTokenExpiresIn;
	}
}
