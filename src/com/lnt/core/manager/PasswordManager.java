package com.lnt.core.manager;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.exception.ValidationException;
import com.lnt.core.common.util.IConstants;

@Component
public class PasswordManager implements IPasswordManager {

	private static final Logger logger = LoggerFactory
			.getLogger(PasswordManager.class);

	@Autowired
	PasswordPolicy passwdPolicy;

	public PasswordPolicy getPasswdPolicy() {
		return passwdPolicy;
	}

	public void setPasswdPolicy(PasswordPolicy passwdPolicy) {
		this.passwdPolicy = passwdPolicy;
	}

	@Override
	public String validatePassword(String userName, int userId, String password)
			throws ServiceApplicationException {
		int passwordMinLength = passwdPolicy.getPasswordMinLength();
		logger.debug("PasswordMinLength: " + passwordMinLength);
		if (password.length() < passwordMinLength)
			throw new ValidationException(IConstants.PASSWORD_MIN_LENGTH);
		if (!isValidControlChars(password))
			throw new ValidationException(
					IConstants.PASSWORD_CONTROL_CHARACTERS);
		if (!isValidCharCriteria(password))
			throw new ValidationException(
					IConstants.PASSWORD_CHARACTER_CRITERIA);
		return IConstants.PASSWORD_VALIDATION_SUCCESS;
	}

	/**
	 * Check for control/non-printing characters in password/username string
	 * 
	 * @param newPassword
	 *            or UserName
	 * @return boolean
	 */
	private boolean isValidControlChars(String newPassword) {
		for (char c : newPassword.toCharArray()) {
			if ((int) c == IConstants.ASCII_DELETE_CHAR
					|| ((int) c >= IConstants.ASCII_NULL_CHAR && (int) c <= IConstants.ASCII_UNIT_SEPERATOR)) {
				logger.debug("User name /Password must not use control or other non-printing characters");
				return false;
			}
		}
		return true;
	}

	/**
	 * Check for valid password character combination criteria Checking if the
	 * password contains characters from at least three of the following four
	 * categories arranged in any order o English uppercase characters (A
	 * through Z) o English lowercase characters (a through z) o Base 10 digits
	 * (0 through 9) o Non-alphabetic characters: ~!@#$%^*&;?.+_
	 * 
	 * @param newPassword
	 *            newly entered password
	 * @return boolean
	 */
	private boolean isValidCharCriteria(String newPassword) {
		int count = 0;

		// String upperCaseRegex = passwdPolicy.getUpperCaseRegex();
		String lowerCaseRegex = passwdPolicy.getLowerCaseRegex();
		String digitsRegex = passwdPolicy.getDigitsRegex();
		String nonAlphaRegex = passwdPolicy.getNonAlphaRegex();

		// logger.debug("UpperCaseRegex: " + upperCaseRegex);
		logger.debug("LowerCaseRegex: " + lowerCaseRegex);
		logger.debug("DigitsRegex: " + digitsRegex);
		logger.debug("NonAlphaRegex: " + nonAlphaRegex);

		/*
		 * English uppercase characters (A through Z) if
		 * (newPassword.matches(upperCaseRegex)) // count++;
		 */

		/* English lowercase characters (a through z) */
		if (newPassword.matches(lowerCaseRegex))
			count++;
		/* Base 10 digits (0 through 9) */
		if (newPassword.matches(digitsRegex))
			count++;
		/* Non-alphabetic characters: ~!@#$%^*&;?.+_ */
		if (newPassword.matches(nonAlphaRegex))
			count++;
		if (count < 2) {
			logger.debug("Password must contain characters from at least three of Uppercase, Lowercase, Numeric and ~!@#$%^*&;?.+_ characters");
			return false;
		}
		return true;
	}

	/**
	 * Generate a random String suitable for use as a temporary password. MinLen
	 * of password is 8 and maxlen of password is 12.
	 * 
	 * @return String suitable for use as a temporary password
	 */
	@Override
	public char[] generateTmpPswd(int minLen, int maxLen, int noOfCAPSAlpha,
			int noOfDigits, int noOfSplChars) {
		if (minLen > maxLen)
			throw new IllegalArgumentException("Min. Length > Max. Length!");
		if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen)
			throw new IllegalArgumentException(
					"Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
		Random rnd = new Random();
		int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
		char[] pswd = new char[len];
		int index = 0;
		for (int i = 0; i < noOfCAPSAlpha; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = IConstants.ALPHA_CAPS.charAt(rnd
					.nextInt(IConstants.ALPHA_CAPS.length()));
		}
		for (int i = 0; i < noOfDigits; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = IConstants.NUM.charAt(rnd.nextInt(IConstants.NUM
					.length()));
		}
		for (int i = 0; i < noOfSplChars; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = IConstants.SPL_CHARS.charAt(rnd
					.nextInt(IConstants.SPL_CHARS.length()));
		}
		for (int i = 0; i < len; i++) {
			if (pswd[i] == 0) {
				pswd[i] = IConstants.ALPHA.charAt(rnd.nextInt(IConstants.ALPHA
						.length()));
			}
		}
		return pswd;
	}

	private int getNextIndex(Random rnd, int len, char[] pswd) {
		int index = rnd.nextInt(len);
		while (pswd[index = rnd.nextInt(len)] != 0)
			;
		return index;
	}

}
