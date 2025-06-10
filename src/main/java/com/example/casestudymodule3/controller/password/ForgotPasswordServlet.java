package com.example.casestudymodule3.controller.password;

import com.example.casestudymodule3.dao.PasswordResetTokenDAO;
import com.example.casestudymodule3.dao.UserDAO;
import com.example.casestudymodule3.model.PasswordResetToken;
import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.util.email.EmailUtil;
import com.example.casestudymodule3.util.security.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        UserDAO userDAO = UserDAO.getInstance();
        User user = userDAO.findByEmail(email);

        String message;
        if (user != null) {
            String token = TokenUtil.generateToken();  // Tạo token
            PasswordResetToken resetToken = new PasswordResetToken(user.getId(), token);

            boolean saved = new PasswordResetTokenDAO().saveToken(resetToken);  // Lưu token vào CSDL

            if (saved) {
                // Tạo link đặt lại mật khẩu
                String contextPath = request.getContextPath();
                String link = "http://localhost:8080" + contextPath + "/reset-password?token=" + token;

                // Gửi email với link khôi phục mật khẩu
                boolean emailSent = EmailUtil.sendEmail(email, "Khôi phục mật khẩu", "Nhấn vào liên kết sau để đặt lại mật khẩu: " + link);

                // Thông báo khi email được gửi thành công
                if (emailSent) {
                    message = "Link khôi phục mật khẩu đã được gửi vào email của bạn. Vui lòng kiểm tra hộp thư để đổi mật khẩu.";
                    request.setAttribute("isEmailSent", true); // Thêm attribute để xác nhận email đã được gửi
                } else {
                    message = "Đã có lỗi xảy ra khi gửi email. Vui lòng thử lại sau.";
                }
            } else {
                message = "Không thể tạo token khôi phục. Vui lòng thử lại sau.";
            }
        } else {
            message = "Email không tồn tại trong hệ thống.";
        }

        // Truyền thông báo và trạng thái vào JSP
        request.setAttribute("message", message);
        request.getRequestDispatcher("view/password/forgot-password.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/password/forgot-password.jsp").forward(request, response);
    }
}

