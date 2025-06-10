package com.example.casestudymodule3.dao;

import com.example.casestudymodule3.model.UserProfile;
import com.example.casestudymodule3.util.connect.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserProfileDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileDAO.class);
    private static final UserProfileDAO INSTANCE = new UserProfileDAO();

    private static final String CHECK_USER_SQL = "SELECT user_id FROM user_profiles WHERE user_id = ?";
    private static final String INSERT_SQL = "INSERT INTO user_profiles (user_id, full_name, avatar_url, dob, gender, bio, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE user_profiles SET full_name=?, avatar_url=?, dob=?, gender=?, bio=?, address=? WHERE user_id=?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM user_profiles WHERE user_id = ?";

    private UserProfileDAO() {}

    public static UserProfileDAO getInstance() {
        return INSTANCE;
    }

    public boolean saveOrUpdateProfile(UserProfile profile) {
        try (Connection conn = DBConnection.getConnection()) {
            if (checkUserExists(conn, profile.getUserId())) {
                return updateProfile(conn, profile);
            } else {
                return insertProfile(conn, profile);
            }
        } catch (SQLException e) {
            logger.error("Error saving or updating user profile for userId: {}", profile.getUserId(), e);
            return false;
        }
    }

    private boolean checkUserExists(Connection conn, int userId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(CHECK_USER_SQL)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean insertProfile(Connection conn, UserProfile profile) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            setInsertParams(stmt, profile);
            return stmt.executeUpdate() > 0;
        }
    }

    private boolean updateProfile(Connection conn, UserProfile profile) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            setUpdateParams(stmt, profile);
            return stmt.executeUpdate() > 0;
        }
    }

    private void setInsertParams(PreparedStatement stmt, UserProfile profile) throws SQLException {
        java.sql.Date dobSql = (profile.getDob() != null) ? new java.sql.Date(profile.getDob().getTime()) : null;

        stmt.setInt(1, profile.getUserId());                  // user_id
        stmt.setString(2, profile.getFullName());             // full_name
        stmt.setString(3, profile.getAvatarUrl());            // avatar_url
        if (dobSql != null) {
            stmt.setDate(4, dobSql);
        } else {
            stmt.setNull(4, Types.DATE);
        }
        stmt.setString(5, profile.getGender());
        stmt.setString(6, profile.getBio());
        stmt.setString(7, profile.getAddress());
    }

    private void setUpdateParams(PreparedStatement stmt, UserProfile profile) throws SQLException {
        java.sql.Date dobSql = (profile.getDob() != null) ? new java.sql.Date(profile.getDob().getTime()) : null;

        stmt.setString(1, profile.getFullName());             // full_name
        stmt.setString(2, profile.getAvatarUrl());            // avatar_url
        if (dobSql != null) {
            stmt.setDate(3, dobSql);
        } else {
            stmt.setNull(3, Types.DATE);
        }
        stmt.setString(4, profile.getGender());
        stmt.setString(5, profile.getBio());
        stmt.setString(6, profile.getAddress());
        stmt.setInt(7, profile.getUserId());                  // WHERE user_id = ?
    }

    public UserProfile getProfileByUserId(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserProfile(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving user profile for userId: {}", userId, e);
        }
        return null;
    }

    private UserProfile extractUserProfile(ResultSet rs) throws SQLException {
        UserProfile profile = new UserProfile();
        profile.setUserId(rs.getInt("user_id"));
        profile.setFullName(rs.getString("full_name"));
        profile.setAvatarUrl(rs.getString("avatar_url"));
        profile.setDob(rs.getDate("dob"));
        profile.setGender(rs.getString("gender"));
        profile.setBio(rs.getString("bio"));
        profile.setAddress(rs.getString("address"));
        return profile;
    }
}
