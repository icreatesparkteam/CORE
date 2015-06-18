package com.lnt.core.common.exception;

public class ServiceRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String responseStatus;
	
	private int code = 500;

	public int getCode() {
		return code;
	}

	/**
	 * Default Constructor
	 */
	public ServiceRuntimeException() {
		super();
	}

	/**
	 * Constructs ServiceRuntimeException with the errorMessage and cause.
	 * 
	 * @param message
	 * @param cause
	 */
	public ServiceRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs ServiceRuntimeException with the errorMessage.
	 * 
	 * @param message
	 */
	public ServiceRuntimeException(String message) {
		super(message);
	}

	/**
	 * Constructs ServiceRuntimeException with cause.
	 * 
	 * @param cause
	 */
	public ServiceRuntimeException(Throwable cause) {
		super(cause);
	}

	public ServiceRuntimeException(String message, int code) {
		super(message);
		this.code = code;
	}

	/**
	 * Method to get the response status to be sent to client.
	 * 
	 * @return String responseStatus
	 */
	public String getResponseStatus() {
		return responseStatus;
	}

	/**
	 * Method to set the response status to be sent to client
	 * 
	 * @param responseStatus
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

}
