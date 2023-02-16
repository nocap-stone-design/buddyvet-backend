package com.capstone.buddyvet.common.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	DEFAULT_ERROR_CODE("5000", HttpStatus.INTERNAL_SERVER_ERROR, "기본 에러 메시지입니다."),

	// Auth 관련
	OAUTH_PROVIDER_CONNECT_ERROR("1000", HttpStatus.NOT_FOUND, "소셜로그인 서버로부터 정보를 받아올 수 없습니다."),
	LOAD_USER_ERROR("1001", HttpStatus.NOT_FOUND, "DB에 존재하지 않는 사용자입니다."),

	// User 관련
	DEACTIVATED_USER("3000", HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다.");



	private final String code;
	private final HttpStatus httpStatus;
	private final String message;
}
