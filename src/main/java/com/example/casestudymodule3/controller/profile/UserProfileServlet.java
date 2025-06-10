package com.example.casestudymodule3.controller.profile;

import com.example.casestudymodule3.dao.UserProfileDAO;
import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.model.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.UUID;

@WebServlet("/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,      // 1MB
        maxFileSize = 5 * 1024 * 1024,                 // 5MB
        maxRequestSize = 10 * 1024 * 1024)             // 10MB
public class UserProfileServlet extends HttpServlet {

    private static final String UPLOAD_PATH = "D:\\uploads";
    private static final String PROFILE_VIEW = "view/profile/profile.jsp";

    private final UserProfileDAO profileDAO = UserProfileDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getAuthenticatedUser(request, response);
        if (user == null) return;

        UserProfile profile = profileDAO.getProfileByUserId(user.getId());
        request.setAttribute("profile", profile);
        request.getRequestDispatcher(PROFILE_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getAuthenticatedUser(request, response);
        if (user == null) return;

        UserProfile profile = buildProfileFromRequest(request, user.getId());

        try {
            profile.setAvatarUrl(handleAvatarUpload(request, user.getId()));
        } catch (IOException | ServletException e) {
            request.setAttribute("error", "Lỗi khi tải ảnh đại diện.");
            request.setAttribute("profile", profile);
            request.getRequestDispatcher(PROFILE_VIEW).forward(request, response);
            return;
        }

        boolean success = profileDAO.saveOrUpdateProfile(profile);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại.");
            request.setAttribute("profile", profile);
            request.getRequestDispatcher(PROFILE_VIEW).forward(request, response);
        }
    }

    private User getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        return user;
    }

    private UserProfile buildProfileFromRequest(HttpServletRequest request, int userId) {
        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setFullName(request.getParameter("fullName"));
        profile.setGender(request.getParameter("gender"));
        profile.setBio(request.getParameter("bio"));
        profile.setAddress(request.getParameter("address"));

        String dobStr = request.getParameter("dob");
        if (dobStr != null && !dobStr.isEmpty()) {
            profile.setDob(Date.valueOf(dobStr));
        }
        return profile;
    }

    private String handleAvatarUpload(HttpServletRequest request, int userId) throws IOException, ServletException {
        Part filePart = request.getPart("avatarFile");
        if (filePart == null || filePart.getSize() == 0) {
            // Lấy lại ảnh cũ nếu không có file mới
            UserProfile existing = profileDAO.getProfileByUserId(userId);
            return (existing != null) ? existing.getAvatarUrl() : null;
        }

        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + originalFileName;

        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new IOException("Không thể tạo thư mục upload: " + UPLOAD_PATH);
        }

        String filePath = UPLOAD_PATH + File.separator + newFileName;
        filePart.write(filePath);

        return request.getContextPath() + "/images?url=" + newFileName;
    }
}
