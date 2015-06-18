package com.lnt.core.manager;

import org.springframework.stereotype.Component;

@Component
public class PasswordPolicy {

	private int passwordMinLength;

	// private String upperCaseRegex;

	private String lowerCaseRegex;

	private String digitsRegex;

	private String nonAlphaRegex;

	public String getNonAlphaRegex() {
		return nonAlphaRegex;
	}

	public void setNonAlphaRegex(String nonAlphaRegex) {
		this.nonAlphaRegex = nonAlphaRegex;
	}

	public int getPasswordMinLength() {
		return passwordMinLength;
	}

	public void setPasswordMinLength(int passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}

	public String getLowerCaseRegex() {
		return lowerCaseRegex;
	}

	public void setLowerCaseRegex(String lowerCaseRegex) {
		this.lowerCaseRegex = lowerCaseRegex;
	}

	public String getDigitsRegex() {
		return digitsRegex;
	}

	public void setDigitsRegex(String digitsRegex) {
		this.digitsRegex = digitsRegex;
	}

}
