package com.lnt.core.common.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class TokenGenerator {

	private static TokenGenerator soleInstance = new TokenGenerator();

	private SecureRandom random = new SecureRandom();

	private TokenGenerator() {
	}

	public static TokenGenerator getInstance() {
		return soleInstance;
	}

	public String generateToken(String id) {
		String token = id + new BigInteger(130, random).toString(32)
				+ System.currentTimeMillis();
		return token;
	}

	public String generateTokenwithoutId() {
		String token = new BigInteger(130, random).toString(32)
				+ System.currentTimeMillis();
		return token;
	}

}
