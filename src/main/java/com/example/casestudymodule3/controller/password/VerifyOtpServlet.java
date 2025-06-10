package com.example.casestudymodule3.controller.password;

import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "VerifyOtpServlet", urlPatterns = "/verify-otp")
public class VerifyOtpServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String otpInput = request.getParameter("otp");

        // Lấy session và dữ liệu người dùng cùng OTP
        HttpSession session = request.getSession();
        String otpSession = (String) session.getAttribute("otp");
        User pendingUser = (User) session.getAttribute("pendingUser");

        // Kiểm tra nếu thông tin trong session hợp lệ
        if (otpInput == null || otpInput.trim().isEmpty()) {
            // Nếu không có OTP nhập vào, thông báo lỗi
            request.setAttribute("error", "Vui lòng nhập mã OTP.");
            request.getRequestDispatcher("view/password/verify.jsp").forward(request, response);
            return;
        }

        // Kiểm tra OTP
        if (pendingUser != null && otpSession != null && otpSession.equals(otpInput)) {
            try {
                // Lưu thông tin người dùng vào cơ sở dữ liệu
                boolean saved = userService.registerUser(pendingUser);

                if (saved) {
                    // Nếu lưu thành công, xóa OTP và user khỏi session
                    session.removeAttribute("otp");
                    session.removeAttribute("pendingUser");

                    // Chuyển hướng về trang đăng nhập
                    response.sendRedirect("view/auth/login.jsp");
                } else {
                    // Nếu lưu thất bại
                    request.setAttribute("error", "Lưu tài khoản thất bại. Vui lòng thử lại.");
                    request.getRequestDispatcher("view/password/verify.jsp").forward(request, response);
                }
            } catch (Exception e) {
                // Xử lý lỗi hệ thống
                request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
                request.getRequestDispatcher("view/password/verify.jsp").forward(request, response);
            }
        } else {
            // Nếu OTP sai
            request.setAttribute("error", "Mã OTP không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("view/password/verify.jsp").forward(request, response);
        }
    }
}



