package com.lnt.core.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	public static boolean mandatory(String field) {
		if (field != null && !field.isEmpty())
			return true;
		return false;
	}

	public static boolean numbersOnly(String field) {
		if (field == null || field.length() == 0)
			return false;

		for (int i = 0; i < field.length(); i++) {
			// If we find a non-digit character we return false.
			if (!Character.isDigit(field.charAt(i)))
				return false;
		}
		return true;
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		boolean isValid = false;
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;

	}

	/*
	 * public static boolean checkuserIdLength(String userId) { // TODO
	 * Auto-generated method stub int userId_length = userId.length();
	 * if(userId_length < IConstants.USERID_MINLENGTH || userId_length >
	 * IConstants.USERID_MAXLENGTH) return false; return true; }
	 */

	/**
	 * Validate email id
	 * 
	 * @param email
	 *            id
	 * @return
	 */
	public static boolean isValidEmail(String email) {
		String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(email);
		return matcher.matches();
	}
}
