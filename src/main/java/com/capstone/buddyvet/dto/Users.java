package com.capstone.buddyvet.dto;

import com.capstone.buddyvet.domain.User;

import lombok.Getter;

@Getter
public class Users {

	/**
	 * 프로필 조회 Response
	 */
	@Getter
	public static class DetailResponse {
		private Long userId;
		private String nickname;

		public DetailResponse(User user) {
			this.userId = user.getId();
			this.nickname = user.getNickname();
		}
	}

	/**
	 * 닉네임 설정 Request
	 */
	@Getter
	public static class AddNicknameRequest {
		private String nickname;
	}
}
