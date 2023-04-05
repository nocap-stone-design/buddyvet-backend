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
	INVALID_TOKEN("1003", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	INVALID_USER("1004", HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자입니다."),
	NOT_ACTIVATED_USER("1004", HttpStatus.BAD_REQUEST, "활동 중이지 않은 사용자입니다."),

	// Buddy 관련
	INVALID_BREED("2000", HttpStatus.BAD_REQUEST, "아이디에 해당하는 품종이 없습니다."),
	INVALID_BUDDY("2001", HttpStatus.BAD_REQUEST, "해당 버디가 존재하지 않습니다."),


	// User 관련
	DEACTIVATED_USER("3000", HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),

	// Diary 관련
	NOT_FOUND_DIARY("4000", HttpStatus.NOT_FOUND, "해당 아이디의 일기가 없습니다."),
	NOT_FOUND_DIARY_IMAGE("4001", HttpStatus.NOT_FOUND, "해당 아이디의 이미지가 일기에 없습니다."),
	INVALID_DATE_FORMAT("4002", HttpStatus.BAD_REQUEST, "잘못된 날짜 형식입니다."),
	INVALID_IMAGE_IDS("4003", HttpStatus.BAD_REQUEST, "잘못된 아이디의 이미지입니다."),

	// File 관련
	FILE_CONVERT_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패했습니다."),
	FILE_UPLOAD_ERROR("5001", HttpStatus.INTERNAL_SERVER_ERROR, "파일 서버 업로드에 실패했습니다."),

	// Community Post 관련
	NOT_FOUND_POST("6000", HttpStatus.NOT_FOUND, "해당 아이디의 게시글이 없습니다."),
	NOT_FOUND_POST_IMAGE("6001", HttpStatus.NOT_FOUND, "해당 아이디의 이미지가 게시글에 없습니다."),;




	private final String code;
	private final HttpStatus httpStatus;
	private final String message;
}
