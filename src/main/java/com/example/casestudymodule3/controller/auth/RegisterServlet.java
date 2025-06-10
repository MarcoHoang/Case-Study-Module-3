package com.example.casestudymodule3.controller.auth;

import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.service.UserService;
import com.example.casestudymodule3.util.Validator;
import com.example.casestudymodule3.util.email.EmailSender;
import com.example.casestudymodule3.util.security.OtpGenerator;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Kiểm tra các trường bắt buộc
        if (name == null || email == null || phone == null || password == null || confirmPassword == null ||
                name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()
                || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {

            request.setAttribute("error", "missing_fields");
            request.setAttribute("error_message", "Vui lòng điền đầy đủ các trường.");
            request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
            return;
        }

        // Validate dữ liệu bằng Validator
        if (Validator.isInvalidEmail(email)) {
            request.setAttribute("error", "invalid_email");
            request.setAttribute("error_message", "Email không hợp lệ.");
            request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
            return;
        }

        if (Validator.isInvalidPhone(phone)) {
            request.setAttribute("error", "invalid_phone");
            request.setAttribute("error_message", "Số điện thoại không hợp lệ.");
            request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
            return;
        }

        if (!Validator.isValidPassword(password)) {
            request.setAttribute("error", "invalid_password");
            request.setAttribute("error_message", "Mật khẩu phải có ít nhất 6 ký tự.");
            request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
            return;
        }

        if (!Validator.isPasswordConfirmed(password, confirmPassword)) {
            request.setAttribute("error", "password_mismatch");
            request.setAttribute("error_message", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
            return;
        }

        try {
            if (userService.isEmailExists(email)) {
                request.setAttribute("error", "email_exists");
                request.setAttribute("error_message", "Email đã được đăng ký. Vui lòng sử dụng email khác.");
                request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
                return;
            }

            if (userService.isPhoneExists(phone)) {
                request.setAttribute("error", "phone_exists");
                request.setAttribute("error_message", "Số điện thoại đã được đăng ký. Vui lòng sử dụng số khác.");
                request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
                return;
            }

            String otp = OtpGenerator.generateOtp();
            User user = new User(0, name, email, phone, password, null, null, "USER", null);

            HttpSession session = request.getSession();
            session.setAttribute("pendingUser", user);
            session.setAttribute("otp", otp);

            // Lưu thời gian hết hạn của OTP (10 phút sau)
            long otpExpiryTime = System.currentTimeMillis() + 10 * 60 * 1000;  // 10 phút
            session.setAttribute("otpExpiryTime", otpExpiryTime);

            boolean mailSent = EmailSender.sendOtpEmail(email, otp);
            if (!mailSent) {
                request.setAttribute("error", "email_failed");
                request.setAttribute("error_message", "Gửi mã OTP thất bại. Vui lòng thử lại sau.");
                request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
                return;
            }

            response.sendRedirect("view/password/verify.jsp");

        } catch (Exception e) {
            request.setAttribute("error", "system_error");
            request.setAttribute("error_message", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("view/auth/register.jsp").forward(request, response);
        }
    }
}
