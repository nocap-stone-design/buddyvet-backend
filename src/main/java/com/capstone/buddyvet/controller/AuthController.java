package com.capstone.buddyvet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.buddyvet.dto.Auth.JwtResponse;
import com.capstone.buddyvet.dto.Auth.LoginRequest;
import com.capstone.buddyvet.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class AuthController {

	private final AuthService authService;

	/**
	 * 로그인/회원가입
	 * @param request AccessToken, Provider
	 * @return jwt, 회원가입 시 201, 로그인 시 200
	 */
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
		return authService.login(request);
	}
}
