package com.lnt.core.common.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.util.Assert;

public class TokenAuthenticationProvider implements AuthenticationProvider,
		InitializingBean, Ordered {

	private static final Logger logger = LoggerFactory
			.getLogger(TokenAuthenticationProvider.class);

	private AuthenticationUserDetailsService authUserDetailsService = null;
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
	private boolean throwExceptionWhenTokenRejected = true;

	private int order = -1; // default: same as non-ordered

	@Override
	public int getOrder() {
		return order;
	}

	public void setOrder(int i) {
		order = i;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(authUserDetailsService,
				"An AuthenticationUserDetailsService must be set");
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		if (authentication.getPrincipal() == null) {
			logger.info("Authentication principal not found in request.");
			if (throwExceptionWhenTokenRejected)
				throw new BadCredentialsException(
						"Authentication principal not found in request");
		}

		UserDetails ud = authUserDetailsService.loadUserDetails(authentication);
		userDetailsChecker.check(ud);

		TokenBasedAuthenticationToken result = new TokenBasedAuthenticationToken(
				ud, authentication.getCredentials(), ud.getAuthorities());
		result.setDetails(authentication.getDetails());

		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return TokenBasedAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public void setThrowExceptionWhenTokenRejected(
			boolean throwExceptionWhenTokenRejected) {
		this.throwExceptionWhenTokenRejected = throwExceptionWhenTokenRejected;
	}

	public void setAuthUserDetailsService(
			AuthenticationUserDetailsService authUserDetailsService) {
		this.authUserDetailsService = authUserDetailsService;
	}

}
