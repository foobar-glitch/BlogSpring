package com.example.blog.mariadb.tempUsers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashingHelper {

    public static String generateRandomSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[8]; // 8 bytes = 64 bits
        secureRandom.nextBytes(salt); // Generate random bytes
        return bytesToHex(salt); // Convert bytes to hex string
    }

    // Helper method to convert bytes to a hex string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)); // Convert each byte to a 2-digit hex value
        }
        return hexString.toString();
    }

    public static String hashPasswordWithSalt(String password, String salt) {

        byte[] passwordBytes = password.getBytes();
        byte[] saltBytes = new BigInteger(salt, 16).toByteArray();
        // Concatenate password and salt
        byte[] passwordWithSalt = new byte[passwordBytes.length + saltBytes.length];

        // Copy the first array into the result
        System.arraycopy(passwordBytes, 0, passwordWithSalt, 0, passwordBytes.length);
        // Copy the second array into the result starting after the first array
        System.arraycopy(saltBytes, 0, passwordWithSalt, passwordBytes.length, saltBytes.length);

        return hashValue(passwordWithSalt);
    }

    public static String hashValue(byte[] value) {
        // Create a MessageDigest instance for SHA-256
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            // Perform the hashing
            byte[] hashBytes = digest.digest(value);

            // Return the hashed password as a hex string
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
