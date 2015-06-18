package com.lnt.core.common.exception;

public class ValidationException extends ServiceApplicationException {

	private static final long serialVersionUID = 1L;

	private static final int code = 400;

	public int getCode() {
		return code;
	}

	public ValidationException(String message) {
		super(code, message);
	}
}
