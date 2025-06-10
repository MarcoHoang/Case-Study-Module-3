package com.example.casestudymodule3.dao;

import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.util.connect.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private static final UserDAO INSTANCE = new UserDAO();

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    // Hàm private tách riêng phần mapping ResultSet -> User
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("password"),
                rs.getString("google_id"),
                rs.getString("verification_code"),
                rs.getString("role"),
                rs.getString("last_login")
        );
    }

    // Hàm private chuẩn hóa set tham số cho các câu SQL INSERT hoặc UPDATE
    private void setUserParameters(PreparedStatement stmt, User user, boolean includeLastLogin) throws SQLException {
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPhone());
        stmt.setString(4, user.getPassword());
        stmt.setString(5, user.getGoogleId());
        stmt.setString(6, user.getVerificationCode());
        stmt.setString(7, user.getRole());
        if (includeLastLogin) {
            stmt.setString(8, user.getLastLogin());
        }
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, phone, password, google_id, verification_code, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Dùng hàm set tham số, không cần last_login
            setUserParameters(stmt, user, false);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error registering user: {}", user.getEmail(), e);
            return false;
        }
    }

    public User loginUser(String email, String hashedPassword) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error logging in user with email: {}", email, e);
        }
        return null;
    }

    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking if email exists: {}", email, e);
        }
        return false;
    }

    public boolean isPhoneExists(String phone) {
        String query = "SELECT COUNT(*) FROM users WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking if phone exists: {}", phone, e);
        }
        return false;
    }

    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email: {}", email, e);
        }
        return null;
    }

    public boolean save(User user) {
        String query = "INSERT INTO users (name, email, phone, password, google_id, verification_code, role, last_login) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setUserParameters(stmt, user, true);  // include last_login

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error saving user: {}", user.getEmail(), e);
            return false;
        }
    }

    public boolean update(User user) {
        String query = "UPDATE users SET name = ?, phone = ?, password = ?, google_id = ?, verification_code = ?, role = ?, last_login = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set các trường tương ứng với câu SQL UPDATE
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getGoogleId());
            stmt.setString(5, user.getVerificationCode());
            stmt.setString(6, user.getRole());
            stmt.setString(7, user.getLastLogin());
            stmt.setString(8, user.getEmail());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating user: {}", user.getEmail(), e);
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating password for userId: {}", userId, e);
            return false;
        }
    }
}
