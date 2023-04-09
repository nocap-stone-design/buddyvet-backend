package com.capstone.buddyvet.security.provider;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.UserState;
import com.capstone.buddyvet.security.service.CustomUserDetailsService;
import com.capstone.buddyvet.security.token.UserJwtAuthenticationToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @todo 애플 인가 작업 후 일괄 예외처리 핸들러로 넘길 예정
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	private static final long EXPIREDTIME = 3 * 24 * 60 * 60 * 1000L;

	private final CustomUserDetailsService userDetailsService;

	public String create(String username, UserState state, String key) {
		Date now = new Date();

		return Jwts.builder()
			.setSubject(username)
			.claim("state", state)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + EXPIREDTIME))
			.signWith(SignatureAlgorithm.HS256, key)
			.compact();
	}

	public Claims getClaims(String token, String key) {
		try {
			return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		} catch (SecurityException e) {
			// log.error(ErrorCode.INVALID_TOKEN_SIGNATURE.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.INVALID_TOKEN_SIGNATURE);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (MalformedJwtException e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.INVALID_TOKEN);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			// log.error(ErrorCode.TOKEN_EXPIRED.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.TOKEN_EXPIRED);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (UnsupportedJwtException e) {
			// log.error(ErrorCode.UNSUPPORTED_TOKEN.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (IllegalArgumentException e) {
			// log.error(ErrorCode.EMPTY_TOKEN_CLAIMS.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.EMPTY_TOKEN_CLAIMS);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (Exception e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		}
	}

	public Claims getClaims(String token, PublicKey key) {
		try {
			return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		} catch (SecurityException e) {
			// log.error(ErrorCode.INVALID_TOKEN_SIGNATURE.getMessage(), e.getMessage());
			// throw new RestApiException(ErrorCode.INVALID_TOKEN_SIGNATURE);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (MalformedJwtException e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			// log.error(ErrorCode.TOKEN_EXPIRED.getMessage(), e.getMessage());
			// throw new RestApiException(ErrorCode.TOKEN_EXPIRED);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (UnsupportedJwtException e) {
			// log.error(ErrorCode.UNSUPPORTED_TOKEN.getMessage(), e.getMessage());
			// throw new RestApiException(ErrorCode.UNSUPPORTED_TOKEN);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (IllegalArgumentException e) {
			// log.error(ErrorCode.EMPTY_TOKEN_CLAIMS.getMessage(), e.getMessage());
			// throw new RestApiException(ErrorCode.EMPTY_TOKEN_CLAIMS);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (Exception e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		}
	}

	public Authentication getAuthentication(String token, String key) {
		String username = this.getClaims(token, key).getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		return new UserJwtAuthenticationToken(userDetails);
	}

	public String getUsername() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return null;
		}

		return ((User)authentication.getPrincipal()).getSocialId();
	}

	public boolean validate(String token, String key) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			return true;
		} catch (SecurityException e) {
			// log.error(ErrorCode.INVALID_TOKEN_SIGNATURE.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.INVALID_TOKEN_SIGNATURE);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (MalformedJwtException e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.INVALID_TOKEN);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			// log.error(ErrorCode.TOKEN_EXPIRED.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.TOKEN_EXPIRED);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (UnsupportedJwtException e) {
			// log.error(ErrorCode.UNSUPPORTED_TOKEN.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (IllegalArgumentException e) {
			// log.error(ErrorCode.EMPTY_TOKEN_CLAIMS.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.EMPTY_TOKEN_CLAIMS);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (Exception e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		}
	}

	public boolean validate(String token, PublicKey key) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			return true;
		} catch (SecurityException e) {
			// log.error(ErrorCode.INVALID_TOKEN_SIGNATURE.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.INVALID_TOKEN_SIGNATURE);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (MalformedJwtException e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.INVALID_TOKEN);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			// log.error(ErrorCode.TOKEN_EXPIRED.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.TOKEN_EXPIRED);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (UnsupportedJwtException e) {
			// log.error(ErrorCode.UNSUPPORTED_TOKEN.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (IllegalArgumentException e) {
			// log.error(ErrorCode.EMPTY_TOKEN_CLAIMS.getMessage(), e.getMessage());
			// throw new CustomException(ErrorCode.EMPTY_TOKEN_CLAIMS);
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		} catch (Exception e) {
			// log.error(ErrorCode.INVALID_TOKEN.getMessage(), e.getMessage());
			throw new RestApiException(ErrorCode.INVALID_TOKEN);
		}
	}

	public String resolve(HttpServletRequest request) {

		String authorization = request.getHeader("Authorization");

		if (authorization == null)
			return null;

		if (authorization.startsWith("Bearer "))
			return authorization.substring(7);

		if (authorization.startsWith("token "))
			return authorization.substring(6);

		throw new RestApiException(ErrorCode.INVALID_TOKEN);
	}
}
