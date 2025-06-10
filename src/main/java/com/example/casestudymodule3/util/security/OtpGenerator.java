package com.example.casestudymodule3.util.security;

import java.util.Random;

public class OtpGenerator {
    public static String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
