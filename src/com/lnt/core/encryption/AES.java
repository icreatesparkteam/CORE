package com.lnt.core.encryption;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AES {
	
	private static String iv = "lnt";
	
	private static String key = "lntkey";

	public static String encrypt(String textToEncrypt) throws Exception {
		byte[] ivBytes = getRawKey(iv.getBytes());
		byte[] input = textToEncrypt.getBytes();
		byte[] keyBytes = generateKey();
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] cipherText = cipher.doFinal(input);
		byte[] resultBytes = Base64.encode(cipherText);
		for (byte b : resultBytes) {

		}

		String result = new String(resultBytes);
		return result;
	}

	public static String decrypt(String textToDecrypt) throws Exception {
		byte[] ivBytes = getRawKey(iv.getBytes());

		String text = textToDecrypt;

		byte[] cipherText = Base64.decode(text);
		byte[] keyBytes = generateKey();
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		byte[] plainText = cipher.doFinal(cipherText);

		String result = new String(plainText);
		return result;
	}

	public static byte[] generateKey() throws Exception {
		PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
		generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes((key)
				.toCharArray()), null, 1);
		KeyParameter params = (KeyParameter) generator
				.generateDerivedParameters(256);

		return params.getKey();
	}

	public static int unsignedToBytes(byte b) {
		return b & 0xFF;
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr);
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}
}
