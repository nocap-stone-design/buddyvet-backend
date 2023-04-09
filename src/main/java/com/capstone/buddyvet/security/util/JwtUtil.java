package com.capstone.buddyvet.security.util;

import java.util.Base64;
import java.util.Map;

import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtUtil {
	// TODO 애플로그인 관련이라 없애도 될듯?

	private static long EXPIRED_TIME = 60 * 60 * 1000;

	public static Map<String, String> getHeader(String token) {
		try {
			String[] chunks = token.split("\\.");
			Base64.Decoder decoder = Base64.getUrlDecoder();
			String header = new String(decoder.decode(chunks[0]));

			ObjectMapper objMapper = new ObjectMapper();

			return objMapper.readValue(header, new TypeReference<Map<String, String>>() {
			});
		} catch (JsonProcessingException e) {
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		}
	}
}
