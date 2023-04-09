package com.capstone.buddyvet.security.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserJwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	public UserJwtAuthenticationToken(Object principal) {
		super(null);
		super.setAuthenticated(true);
		this.principal = principal;
	}

	public UserJwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal) {
		super(authorities);
		super.setAuthenticated(true);
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

}
