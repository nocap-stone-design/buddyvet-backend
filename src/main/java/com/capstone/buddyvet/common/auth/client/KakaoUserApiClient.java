package com.capstone.buddyvet.common.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone.buddyvet.common.auth.dto.Kakao;
import com.capstone.buddyvet.common.config.FeignClientConfig;

@FeignClient(name = "kakaoClient", url = "https://kapi.kakao.com", configuration = FeignClientConfig.class)
public interface KakaoUserApiClient {

	@PostMapping("/v2/user/me")
	Kakao.User getUserInfo(@RequestHeader("Authorization") String accessToken);

	// @PostMapping("/v1/user/unlink")
	// KakaoUnlinkResponse unlinkUser(@RequestHeader("Authorization") String appAdminKey,
	// 	@RequestParam("target_id_type") String targetIdType,
	// 	@RequestParam("target_id") String targetId);
}
