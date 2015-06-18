package com.lnt.core.common.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.lnt.core.common.exception.InvalidTokenException;
import com.lnt.core.common.util.CookieUtil;
import com.lnt.core.common.util.IConstants;

public class AuthFilterImpl extends GenericFilterBean implements
		InitializingBean, ApplicationEventPublisherAware {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthFilterImpl.class);

	private ApplicationEventPublisher eventPublisher = null;
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private AuthenticationManager authenticationManager = null;

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationManager,
				"An AuthenticationManager must be set");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.info("AuthFilterImpl:doFilter Method");
		try {
			doAuthenticate((HttpServletRequest) request,
					(HttpServletResponse) response);
		} catch (AuthenticationException failed) {
			logger.error(
					"AuthFilterImpl:doFilter Method Authentication Failed ........... : {}",
					failed.getMessage());
			unsuccessfulAuthentication((HttpServletRequest) request,
					(HttpServletResponse) response, failed);
			return;
		}

		chain.doFilter(request, response);

	}

	private void doAuthenticate(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.info("AuthFilterImpl:doAuthenticate Method");
		Object token = getAuthenticationToken(request);
		logger.info("AuthFilterImpl:doAuthenticate Method token " + token);
		if (token == null) {
			logger.info("AuthFilterImpl:doAuthenticate Method Authentication token not found in request");
			AuthenticationException failed = new InvalidTokenException(
					"Authentication token not found in request");
			unsuccessfulAuthentication(request, response, failed);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Authentication Failed: " + failed.getMessage());
		}

		AbstractAuthenticationToken authRequest = new TokenBasedAuthenticationToken(
				token, token);
		authRequest.setDetails(authenticationDetailsSource
				.buildDetails(request));
		Authentication authResult = authenticationManager
				.authenticate(authRequest);
		successfulAuthentication(request, response, authResult);
	}

	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult) {
		logger.info("AuthFilterImpl:successfulAuthentication Method  Authentication success: "
				+ authResult);
		SecurityContextHolder.getContext().setAuthentication(authResult);
		if (this.eventPublisher != null) {
			eventPublisher
					.publishEvent(new InteractiveAuthenticationSuccessEvent(
							authResult, this.getClass()));
		}
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException {
		SecurityContextHolder.clearContext();

		if (logger.isDebugEnabled()) {
			logger.debug(
					"AuthFilterImpl:unsuccessfulAuthentication Method Cleared security context due to exception",
					failed);
		}

		request.getSession().setAttribute(
				WebAttributes.AUTHENTICATION_EXCEPTION, failed);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED,
				failed.getMessage());
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher anApplicationEventPublisher) {
		this.eventPublisher = anApplicationEventPublisher;

	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource,
				"AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	private Object getAuthenticationToken(HttpServletRequest req) {
		logger.info("AuthFilterImpl:getAuthentictaionToken Method ");
		String token = req.getHeader(IConstants.TOKEN_HEADER_KEY);
		if (token == null || token.isEmpty()) {
			logger.info("AuthFilterImpl:getAuthentictaionToken Method Token not found, checking for cookie ......");
			Cookie cookie = CookieUtil.getCookie(req,
					IConstants.TOKEN_HEADER_KEY);
			if (cookie != null) {
				token = cookie.getValue();
				logger.info("AuthFilterImpl:getAuthentictaionToken Method Token from cookie "
						+ token);
			}
		}
		return token;
	}

}
