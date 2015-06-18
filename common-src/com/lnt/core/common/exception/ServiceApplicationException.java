package com.lnt.core.common.exception;

public class ServiceApplicationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String responseStatus;
	
	private int code = 400;
	
	private String reason;

	public int getCode() {
		return code;
	}

	/**
	 * Constructs ServiceApplicationException with the specified cause and a
	 * detail message in the exception.
	 * 
	 * @param exception
	 */
	public ServiceApplicationException(Throwable exception) {
		super(exception);
	}

	/**
	 * Constructs ServiceApplicationException with the specified detail message
	 * and cause in the exception.
	 * 
	 * @param message
	 * @param i
	 */
	public ServiceApplicationException(String message, Throwable i) {
		super(message, i);
	}

	/**
	 * Constructs ServiceApplicationException with error message,
	 * responseStatus.
	 * 
	 * @param message
	 * @param responseStatus
	 * @param exception
	 */
	public ServiceApplicationException(String message, String responseStatus,
			Throwable exception) {
		super(message, exception);
		this.responseStatus = responseStatus;
	}

	public ServiceApplicationException(String message, int code,
			Throwable exception) {
		super(message, exception);
		this.code = code;
		this.reason = exception.toString();
	}

	/**
	 * Constructs ServiceApplicationException with the specified detail message.
	 * 
	 * @param message
	 */
	public ServiceApplicationException(String message) {
		super(message);
	}

	/**
	 * Method to get the response status to be sent to client
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

	public ServiceApplicationException(int code, String message) {
		super(message);
		this.code = code;
	}

}
