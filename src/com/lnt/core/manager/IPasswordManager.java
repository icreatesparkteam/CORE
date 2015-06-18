package com.lnt.core.manager;

import com.lnt.core.common.exception.ServiceApplicationException;

public interface IPasswordManager {

	/**
	 * Validate password pre user authorization.
	 * 
	 * @param userName
	 * @param userId
	 * @param password
	 * @return message
	 * @throws ServiceApplicationException
	 */
	public String validatePassword(String userName, int userId, String password)
			throws ServiceApplicationException;

	/**
	 * Generate Generate a random String suitable for use as a temporary
	 * password. MinLen of password is 8 and maxlen of password is 12.
	 * 
	 * @param minLen
	 *            Minlen of temporary password is 8
	 * @param maxLen
	 *            Maxlen of temporary password is 12
	 * @param noOfCAPSAlpha
	 *            - Number of CAPSAlpha is 1
	 * @param noOfDigits
	 *            - Number of noOfDigits is 1
	 * @param noOfSplChars
	 *            - Number of noOfSplChars is 1
	 * @return String suitable for use as a temporary password
	 */
	public char[] generateTmpPswd(int minLen, int maxLen, int noOfCAPSAlpha,
			int noOfDigits, int noOfSplChars);

}
