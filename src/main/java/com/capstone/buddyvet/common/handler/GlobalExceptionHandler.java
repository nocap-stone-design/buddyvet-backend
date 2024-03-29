package com.capstone.buddyvet.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.capstone.buddyvet.common.dto.ResponseDto;
import com.capstone.buddyvet.common.exception.RestApiException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {RestApiException.class})
	public ResponseEntity<Object> handleApiException(RestApiException e) {
		log.error("handleApiException throw RestApiException : {}", e.getErrorCode());
		return ResponseDto.toResponseEntity(e);
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleException(Exception e) {
		log.error("handleException throw Exception : {}", e.getMessage());
		return ResponseDto.toResponseEntity(e);
	}
}
