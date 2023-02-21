package com.capstone.buddyvet.common.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	DEFAULT_ERROR_CODE("10000", HttpStatus.INTERNAL_SERVER_ERROR, "기본 에러 메시지입니다."),

	// Auth 관련
	OAUTH_PROVIDER_CONNECT_ERROR("1000", HttpStatus.NOT_FOUND, "소셜로그인 서버로부터 정보를 받아올 수 없습니다."),
	LOAD_USER_ERROR("1001", HttpStatus.NOT_FOUND, "DB에 존재하지 않는 사용자입니다."),
	INVALID_ACCESS("1002", HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),


	// User 관련
	DEACTIVATED_USER("3000", HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),

	// Diary 관련
	NOT_FOUND_DIARY("4000", HttpStatus.NOT_FOUND, "해당 아이디의 일기가 없습니다."),
	NOT_FOUND_DIARY_IMAGE("4001", HttpStatus.NOT_FOUND, "해당 아이디의 이미지가 일기에 없습니다."),

	// File 관련
	FILE_CONVERT_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패했습니다."),
	FILE_UPLOAD_ERROR("5001", HttpStatus.INTERNAL_SERVER_ERROR, "파일 서버 업로드에 실패했습니다.");


	private final String code;
	private final HttpStatus httpStatus;
	private final String message;
}
