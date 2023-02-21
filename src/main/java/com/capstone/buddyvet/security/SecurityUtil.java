package com.capstone.buddyvet.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityUtil {

  /**
   * jwt 를 이용해 현재 로그인 유저를 판단
   * @return 현재 SecurityContext 에 저장된 username
   */
  public static Optional<String> getCurrentUsername() {

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      log.debug("Security Context에 인증 정보가 없습니다.");
      return Optional.empty();
    }

    String username = null;
    if (authentication.getPrincipal() instanceof UserDetails) {
      UserDetails springSecurityUser = (UserDetails)authentication.getPrincipal();
      username = springSecurityUser.getUsername();
    } else if (authentication.getPrincipal() instanceof String) {
      username = (String)authentication.getPrincipal();
    }

    return Optional.ofNullable(username);
  }
}

