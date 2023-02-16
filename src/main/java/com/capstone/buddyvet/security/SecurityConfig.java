package com.capstone.buddyvet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity    //기본적인 Web 보안 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true)    //@PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해 적용
public class SecurityConfig {

	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	public SecurityConfig(TokenProvider tokenProvider,
		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
		JwtAccessDeniedHandler jwtAccessDeniedHandler) {
		this.tokenProvider = tokenProvider;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()

			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler)

			// h2-console 을 위한 설정 START
			.and()
			.headers()
			.frameOptions()
			.sameOrigin()

			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)    // 세션을 사용하지 않기 때문에 세션 설정 STATELESS 로 설정

			.and()
			.authorizeRequests()    //HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다
			.antMatchers("/api/**/oauth/**").permitAll()
			.antMatchers("/api/**/weather").permitAll()
			.anyRequest().permitAll()

			.and()
			.apply(new JwtSecurityConfig(tokenProvider));

		return http.build();
	}
}