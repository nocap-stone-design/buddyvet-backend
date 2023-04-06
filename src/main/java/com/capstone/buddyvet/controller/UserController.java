package com.capstone.buddyvet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.buddyvet.common.dto.ResponseDto;
import com.capstone.buddyvet.dto.Users;
import com.capstone.buddyvet.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService userService;

	/**
	 * 유저 프로필 조회 API
	 * @return 유저 id, 닉네임
	 */
	@GetMapping
	public ResponseDto<Users.DetailResponse> userDetail() {
		return new ResponseDto<>(userService.getUser());
	}

	/**
	 * 유저 닉네임 입력 API
	 * @param request 설정할 닉네임
	 * @return void
	 */
	@PostMapping("/nickname")
	public ResponseDto userNicknameAdd(@RequestBody Users.AddNicknameRequest request) {
		userService.addUserNickname(request);
		return new ResponseDto(null);
	}
}
