package com.lnt.core.common.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	private static final int code = 203;

	public int getCode() {
		return code;
	}

	public TokenExpiredException(String message) {
		super(message);
	}

	public TokenExpiredException(String msg, Throwable t) {
		super(msg, t);
	}
}
