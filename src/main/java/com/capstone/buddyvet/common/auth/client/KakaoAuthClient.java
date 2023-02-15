package com.capstone.buddyvet.common.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone.buddyvet.common.auth.dto.KakaoToken;
import com.capstone.buddyvet.common.config.FeignClientConfig;

@FeignClient(name = "kakaoAuthClient", url = "https://kauth.kakao.com", configuration = FeignClientConfig.class)
public interface KakaoAuthClient {

	@PostMapping(value = "/oauth/token")
	KakaoToken getToken(@RequestParam("client_id") String restApiKey,
		@RequestParam("redirect_uri") String redirectUrl,
		@RequestParam("code") String code,
		@RequestParam("grant_type") String grantType);
}
