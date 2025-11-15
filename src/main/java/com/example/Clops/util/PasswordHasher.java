package com.example.Clops.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class PasswordHasher {

    private static final String ALGORITHM = "SHA-256";
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(password.getBytes());
            return HEX_FORMAT.formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm " + ALGORITHM + " not available", e);
        }
    }

    public boolean verifyPassword(String password, String passwordHash) {
        String computedHash = hashPassword(password);
        return computedHash.equals(passwordHash);
    }

    public boolean isValidHash(String hash) {
        // SHA-256 хэш должен быть 64 символа в hex-формате
        return hash != null && hash.matches("^[a-fA-F0-9]{64}$");
    }
}