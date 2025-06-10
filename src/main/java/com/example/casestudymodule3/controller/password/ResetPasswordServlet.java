package com.example.casestudymodule3.controller.password;

import com.example.casestudymodule3.dao.PasswordResetTokenDAO;
import com.example.casestudymodule3.dao.UserDAO;
import com.example.casestudymodule3.model.PasswordResetToken;
import com.example.casestudymodule3.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();
    private final PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (!tokenDAO.isTokenValid(token)) {
            request.setAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
        } else {
            request.setAttribute("token", token);
        }

        request.getRequestDispatcher("view/password/reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
            request.setAttribute("newPassword", newPassword);
            request.setAttribute("confirmPassword", confirmPassword);
            request.getRequestDispatcher("view/password/reset-password.jsp").forward(request, response);
            return;
        }

        if (tokenDAO.isTokenValid(token)) {
            PasswordResetToken resetToken = tokenDAO.findByToken(token);
            int userId = resetToken.getUserId();
            String hashedPassword = userService.hashPassword(newPassword);

            boolean updated = UserDAO.getInstance().updatePassword(userId, hashedPassword);

            if (updated) {
                boolean deleted = tokenDAO.deleteToken(token);
                if (deleted) {
                    request.setAttribute("message", "Mật khẩu đã được cập nhật thành công. Bạn có thể đăng nhập lại.");
                    request.getRequestDispatcher("view/auth/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Mật khẩu đã được cập nhật nhưng không thể xóa token. Vui lòng liên hệ quản trị viên.");
                    request.getRequestDispatcher("view/password/reset-password.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Không thể cập nhật mật khẩu. Vui lòng thử lại sau.");
                request.getRequestDispatcher("view/password/reset-password.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("view/password/reset-password.jsp").forward(request, response);
        }
    }
}


