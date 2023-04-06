package com.capstone.buddyvet.dto;

import com.capstone.buddyvet.domain.User;

import lombok.Getter;

@Getter
public class Users {

	@Getter
	public static class DetailResponse {
		private Long userId;
		private String nickname;

		public DetailResponse(User user) {
			this.userId = user.getId();
			this.nickname = user.getNickname();
		}
	}
}
