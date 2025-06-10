package com.example.casestudymodule3.util;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[35789][0-9]{8}$");

    // Kiểm tra định dạng email
    public static boolean isInvalidEmail(String email) {
        if (email == null) return true; // Email không hợp lệ
        return !EMAIL_PATTERN.matcher(email).matches(); // Nếu không khớp regex, là không hợp lệ
    }

    // Kiểm tra định dạng số điện thoại (10 số, bắt đầu bằng các đầu 03, 05, 07, 08, 09)
    public static boolean isInvalidPhone(String phone) {
        if (phone == null) return true; // Số điện thoại không hợp lệ
        return !PHONE_PATTERN.matcher(phone).matches(); // Nếu không khớp regex, là không hợp lệ
    }


    // Kiểm tra mật khẩu có tối thiểu 6 ký tự
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Kiểm tra mật khẩu và mật khẩu xác nhận có trùng nhau hay không
    public static boolean isPasswordConfirmed(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }
}
