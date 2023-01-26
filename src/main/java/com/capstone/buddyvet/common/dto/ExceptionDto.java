package com.capstone.buddyvet.common.dto;

import com.capstone.buddyvet.common.enums.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ExceptionDto {
	private final String code;
	private final String message;

	public ExceptionDto(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}
}
