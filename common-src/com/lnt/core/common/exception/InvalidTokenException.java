package com.lnt.core.common.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	private static final int code = 400;

	public int getCode() {
		return code;
	}

	public InvalidTokenException(String message) {
		super(message);
	}

	public InvalidTokenException(String message, Throwable t) {
		super(message, t);
	}

}
