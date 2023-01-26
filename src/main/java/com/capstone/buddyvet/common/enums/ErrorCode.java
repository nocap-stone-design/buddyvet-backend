package com.capstone.buddyvet.common.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	DEFAULT_ERROR_CODE("5000", HttpStatus.INTERNAL_SERVER_ERROR, "기본 에러 메시지입니다.");

	private final String code;
	private final HttpStatus httpStatus;
	private final String message;
}
