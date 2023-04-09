package com.capstone.buddyvet.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.capstone.buddyvet.security.filter.JwtAuthenticationFilter;
import com.capstone.buddyvet.security.provider.JwtTokenProvider;
import com.capstone.buddyvet.security.provider.UserJwtAuthenticationProvider;
import com.capstone.buddyvet.security.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final Properties properties;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new UserJwtAuthenticationProvider(userDetailsService);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.httpBasic()
			.disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/**")
			.permitAll()    // @Todo URI 결정 후 접근 제어 변경
			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, properties),
				UsernamePasswordAuthenticationFilter.class);
	}
}
