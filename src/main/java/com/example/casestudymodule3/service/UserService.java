package com.example.casestudymodule3.service;

import com.example.casestudymodule3.dao.UserDAO;
import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.util.Validator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class UserService implements IUserService{

    private static final UserService INSTANCE = new UserService();

    private final UserDAO userDAO = UserDAO.getInstance();

    private static final SecureRandom random = new SecureRandom();

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    // Mã hóa mật khẩu người dùng với SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Thay printStackTrace bằng logging hoặc throw RuntimeException
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Tạo mã xác minh ngẫu nhiên 6 chữ số, dùng SecureRandom để bảo mật hơn
    public String generateVerificationCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Kiểm tra email đã tồn tại trong cơ sở dữ liệu
    public boolean isEmailExists(String email) {
        return userDAO.isEmailExists(email);
    }

    // Kiểm tra số điện thoại đã tồn tại trong cơ sở dữ liệu
    public boolean isPhoneExists(String phone) {
        return userDAO.isPhoneExists(phone);
    }

    // Đăng ký người dùng
    public boolean registerUser(User user) {
        if (Validator.isInvalidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (Validator.isInvalidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (isPhoneExists(user.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }

        user.setPassword(hashPassword(user.getPassword()));
        user.setVerificationCode(generateVerificationCode());

        return userDAO.registerUser(user);
    }

    // Đăng nhập người dùng
    public User loginUser(String email, String password) {
        // Hash password trước khi so sánh
        String hashedPassword = hashPassword(password);
        return userDAO.loginUser(email, hashedPassword);
    }

    // Tìm user theo email
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    // Lưu user mới (insert)
    public boolean save(User user) {
        return userDAO.save(user);
    }

    // Cập nhật user (update)
    public boolean update(User user) {
        return userDAO.update(user);
    }

}
