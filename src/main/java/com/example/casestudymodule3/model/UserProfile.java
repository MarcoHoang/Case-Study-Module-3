package com.example.casestudymodule3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private int userId;
    private String fullName;
    private String avatarUrl;
    private Date dob;
    private String gender;
    private String bio;
    private String address;

}
