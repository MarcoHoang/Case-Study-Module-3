package com.example.casestudymodule3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {
    private int userId;
    private String resetToken;
    private Timestamp createdAt;
    private Timestamp expiresAt;

    public PasswordResetToken(int userId, String resetToken) {
        this.userId = userId;
        this.resetToken = resetToken;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.expiresAt = new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000);
    }

}
