package com.capstone.buddyvet.common.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.buddyvet.common.auth.client.KakaoUserApiClient;
import com.capstone.buddyvet.common.auth.dto.KakaoUserInfo;
import com.capstone.buddyvet.domain.enums.Provider;
import com.capstone.buddyvet.dto.Auth.LoginRequest;
import com.capstone.buddyvet.dto.Auth.SocialUserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoAuthService {

	@Value("${oauth2.kakao.admin-key}")
	private String adminKey;

	private final KakaoUserApiClient kakaoUserApiClient;

	/**
	 * 카카오 유저 정보 조회
	 * @param loginRequest
	 * @return 카카오 ID, 닉네임
	 */
	public Optional<SocialUserInfo> getUserInfo(LoginRequest loginRequest) {

		KakaoUserInfo userInfo = kakaoUserApiClient.getUserInfo("Bearer " + loginRequest.getAccessToken());

		return Optional.of(SocialUserInfo.builder()
			.loginId(userInfo.getId())
			.providerType(Provider.KAKAO)
			.nickname(userInfo.getKakaoAccount().getProfile().getNickname())
			.build());
	}

	/**
	 * 카카오 회원탈퇴
	 * @param targetId 카카오 ID
	 */
	public void revoke(String targetId) {
		kakaoUserApiClient.unlinkUser("KakaoAK " + adminKey, "user_id", targetId);
	}
}
