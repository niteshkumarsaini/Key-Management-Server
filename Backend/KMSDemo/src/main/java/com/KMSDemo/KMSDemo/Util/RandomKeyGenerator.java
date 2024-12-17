package com.KMSDemo.KMSDemo.Util;
import java.security.SecureRandom;
import java.util.Base64;

public class RandomKeyGenerator {
    // Generate a random key of a specified length in bytes
    public static String generateRandomKey(int keyLengthInBytes) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[keyLengthInBytes];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}

