package com.lnt.core.common.util;

import java.util.Date;

public class DateUtil {
	static String dateFormat = "dd/MM/yy HH:mm:ss";

	public static boolean isExpired(Date date, int expiryMins) {
		int expiryMilliSec = expiryMins * 60 * 1000;
		long origTime = date.getTime();
		long currentTime = System.currentTimeMillis();
		long expiryTime = origTime + expiryMilliSec;

		if (currentTime < expiryTime)
			return false;
		return true;
	}

	public static boolean isExpiredActivationCode(Date startdate, int expiryDays) {
		int expiryMilliSec = expiryDays * 24 * 60 * 60 * 1000;
		long startTime = startdate.getTime();
		long currentTime = System.currentTimeMillis();
		long expiryTime = startTime + expiryMilliSec;

		if (currentTime < expiryTime)
			return false;
		return true;
	}

	/*
	 * public static DateTime getDateTime(String dateTime){ DateTimeFormatter
	 * format = DateTimeFormat.forPattern(dateFormat); return
	 * format.parseDateTime(dateTime);
	 * 
	 * }
	 */

}
