package com.example.casestudymodule3.service;

import com.example.casestudymodule3.model.User;

public interface IUserService {

    String hashPassword(String password);

    String generateVerificationCode();

    boolean isEmailExists(String email);

    boolean isPhoneExists(String phone);

    boolean registerUser(User user);

    User loginUser(String email, String password);

    User findByEmail(String email);

    boolean save(User user);

    boolean update(User user);
}
