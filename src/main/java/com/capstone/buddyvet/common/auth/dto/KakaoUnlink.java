package com.capstone.buddyvet.common.auth.dto;

import lombok.Getter;

@Getter
public class KakaoUnlink {
	private String target_id_type;
	private String target_id;

	public KakaoUnlink(String target_id) {
		this.target_id_type = "user_id";
		this.target_id = target_id;
	}
}
