package com.example.casestudymodule3.util.security;

import java.security.SecureRandom;

public class TokenUtil {
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 20;

    // Tạo SecureRandom một lần, tái sử dụng để tránh tốn chi phí khởi tạo
    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(CHARSET.length());
            token.append(CHARSET.charAt(index));
        }

        return token.toString();
    }
}
