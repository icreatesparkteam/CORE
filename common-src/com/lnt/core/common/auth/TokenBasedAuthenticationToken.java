package com.lnt.core.common.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class TokenBasedAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private Object token;

	public TokenBasedAuthenticationToken(Object aPrincipal, Object aCredentials) {
		super(null);
		this.token = aPrincipal;
	}

	public TokenBasedAuthenticationToken(Object aPrincipal,
			Object aCredentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.token = aPrincipal;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

}
