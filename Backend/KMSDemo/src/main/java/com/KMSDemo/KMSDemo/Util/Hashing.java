package com.KMSDemo.KMSDemo.Util;
import java.security.MessageDigest;
import java.util.Base64;
public class Hashing {

	public static String calculateSHA256Hash(String input) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hashBytes = digest.digest(input.getBytes());
	        return Base64.getEncoder().encodeToString(hashBytes);
	    } catch (Exception e) {
	        throw new RuntimeException("Error calculating hash", e);
	    }
	}

}
