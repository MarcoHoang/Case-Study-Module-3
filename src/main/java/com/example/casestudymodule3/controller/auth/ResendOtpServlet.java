package com.example.casestudymodule3.controller.auth;

import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.util.security.OtpGenerator;
import com.example.casestudymodule3.util.email.EmailSender;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ResendOtpServlet", urlPatterns = "/resendOtp")
public class ResendOtpServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ResendOtpServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User pendingUser = (User) session.getAttribute("pendingUser");

        if (pendingUser != null) {
            // Tạo OTP mới
            String otp = OtpGenerator.generateOtp();
            session.setAttribute("otp", otp);

            boolean sent = EmailSender.sendOtpEmail(pendingUser.getEmail(), otp);

            if (sent) {
                request.setAttribute("message", "Mã OTP đã được gửi lại vào email của bạn. Vui lòng kiểm tra.");
            } else {
                request.setAttribute("error", "Lỗi khi gửi lại OTP. Vui lòng thử lại.");
                logger.error("Failed to resend OTP email to {}", pendingUser.getEmail());
            }

        } else {
            request.setAttribute("error", "Không tìm thấy thông tin người dùng. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("view/password/verify.jsp").forward(request, response);
    }
}

