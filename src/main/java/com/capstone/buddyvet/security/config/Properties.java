package com.capstone.buddyvet.security.config;

import javax.annotation.PostConstruct;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class Properties {

	private final Environment environment;
	@Getter
	private String secret;

	/**
	 * @Todo 추후 배포 단계에서 로깅 제거
	 */
	@PostConstruct
	public void init() {
		this.secret = environment.getProperty("JWT_SECRET");
		log.info("JWT_SECRET is " + this.secret);
	}
}
