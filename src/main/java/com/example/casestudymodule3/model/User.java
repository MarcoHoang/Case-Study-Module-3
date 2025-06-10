package com.example.casestudymodule3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String googleId;
    private String verificationCode;
    private String role;
    private String lastLogin;
}


