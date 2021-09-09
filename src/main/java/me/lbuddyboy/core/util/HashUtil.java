package me.lbuddyboy.core.util;

import lombok.SneakyThrows;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:46 PM
 * LBuddyBoy Development / me.lbuddyboy.core.util
 */
public class HashUtil {

	@SneakyThrows
	public static String hash(String input) {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
		BigInteger number = new BigInteger(1, hash);
		StringBuilder hexString = new StringBuilder(number.toString(16));

		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}

}
