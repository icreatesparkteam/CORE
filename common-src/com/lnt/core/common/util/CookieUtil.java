package com.lnt.core.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(CookieUtil.class);
	private static final int DEFAULT_MAX_AGE = 24 * 60 * 60;

	public static void setCookie(HttpServletResponse response, String name,
			String value) {
		setCookieWithAge(response, name, value, DEFAULT_MAX_AGE);
	}

	public static void setCookieWithAge(HttpServletResponse response,
			String name, String value, int age) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(age);
		cookie.setPath("/");
		// Start JUNE 2013 Security issue to add secure attribute to cookies
		// cookie.setSecure(true);
		// End JUNE 2013 Security issue to add secure attribute to cookies
		response.addCookie(cookie);
	}

	public static Cookie getCookie(HttpServletRequest request, String name) {
		if (name == null || request == null) {
			throw new IllegalArgumentException("cookie name is null");
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				logger.info("....... Cookie Name ..... : {}", cookie.getName());
				if (name.equals(cookie.getName())) {
					if (!IConstants.EMPTY_STRING.equals(cookie.getValue()))
						return cookie;
				}
			}
		}
		return null;
	}

	public static void deleteCookie(HttpServletResponse response, String name,
			HttpServletRequest request) {
		Cookie cookie;
		if (request != null) {
			cookie = getCookie(request, name);
			if (cookie == null) {
				logger.info("@deleteCookie: cookie null");
				cookie = new Cookie(name, IConstants.EMPTY_STRING);
			} else {
				logger.info("@deleteCookie: cookie not null");
				cookie.setValue(IConstants.EMPTY_STRING);
			}
		} else {
			cookie = new Cookie(name, IConstants.EMPTY_STRING);
		}

		cookie.setMaxAge(0); // An age of 0 is defined to mean "delete cookie"
		cookie.setPath("/"); // for all subdirs
		response.addCookie(cookie);
	}

}
