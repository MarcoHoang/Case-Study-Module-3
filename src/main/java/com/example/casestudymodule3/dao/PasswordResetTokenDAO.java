package com.example.casestudymodule3.dao;

import com.example.casestudymodule3.model.PasswordResetToken;
import com.example.casestudymodule3.util.connect.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class PasswordResetTokenDAO {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenDAO.class);

    // Lưu token reset password mới
    public boolean saveToken(PasswordResetToken token) {
        String query = "INSERT INTO password_reset_tokens (user_id, token, created_at, expires_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, token.getUserId());
            stmt.setString(2, token.getResetToken());
            stmt.setTimestamp(3, token.getCreatedAt());
            stmt.setTimestamp(4, token.getExpiresAt());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error saving password reset token for userId: {}", token.getUserId(), e);
        }
        return false;
    }

    // Kiểm tra xem token có hợp lệ hay không (chưa hết hạn)
    public boolean isTokenValid(String token) {
        String query = "SELECT * FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp expiresAt = rs.getTimestamp("expires_at");
                return new Timestamp(System.currentTimeMillis()).before(expiresAt);
            }
        } catch (SQLException e) {
            logger.error("Error validating password reset token: {}", token, e);
        }
        return false;
    }

    // Xóa token sau khi đã sử dụng
    public boolean deleteToken(String token) {
        String query = "DELETE FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting password reset token: {}", token, e);
        }
        return false;
    }

    public PasswordResetToken findByToken(String token) {
        String query = "SELECT * FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                return new PasswordResetToken(userId, token);
            }
        } catch (SQLException e) {
            logger.error("Error finding password reset token: {}", token, e);
        }
        return null;
    }
}
