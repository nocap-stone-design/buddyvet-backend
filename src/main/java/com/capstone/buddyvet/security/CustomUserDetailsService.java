package com.capstone.buddyvet.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userId) {
		User user = userRepository.findById(Long.valueOf(userId))
			.orElseThrow(() -> new RestApiException(ErrorCode.LOAD_USER_ERROR));
		return createSecurityUser(userId, user);
	}

	private org.springframework.security.core.userdetails.User createSecurityUser(String userId, User user) {
		if (!user.isActivate()) {
			throw new RestApiException(ErrorCode.DEACTIVATED_USER);
		}
		return new org.springframework.security.core.userdetails.User(
			userId, passwordEncoder.encode("password"),
			Collections.singleton(new SimpleGrantedAuthority(user.getRoleType().getCode())));
	}
}
