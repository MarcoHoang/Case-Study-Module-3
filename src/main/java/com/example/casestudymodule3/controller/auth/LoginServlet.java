package com.example.casestudymodule3.controller.auth;

import com.example.casestudymodule3.service.UserService;
import com.example.casestudymodule3.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String setEmail = request.getParameter("setEmail");
        if (setEmail != null) {
            // Lưu email vào session để chuyển sang bước nhập mật khẩu
            request.getSession().setAttribute("email", setEmail);
            response.sendRedirect(request.getContextPath() + "/view/auth/login.jsp");
            return;
        }
        request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response); // Chuyển hướng đến trang đăng
        // nhập
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String resetEmail = request.getParameter("resetEmail");

        // ✅ 1. Xử lý reset email → Quay lại bước nhập email
        if ("true".equals(resetEmail)) {
            session.removeAttribute("email");
            response.sendRedirect(request.getContextPath() + "/view/auth/login.jsp");
            return;
        }

        // ✅ 2. Xử lý AJAX kiểm tra email tồn tại
        if (password == null) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            boolean exists = userService.isEmailExists(email);
            out.print("{\"exists\":" + exists + "}");
            out.flush();
            return;
        }

        // ✅ 3. Kiểm tra đầu vào đầy đủ
        if (email == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error_message", "Vui lòng điền đầy đủ email và mật khẩu.");
            session.removeAttribute("email");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
            return;
        }

        // ✅ 4. Xử lý đăng nhập
        User user = userService.loginUser(email, password);

        if (user != null) {
            // Đăng nhập thành công
            session.setAttribute("user", user);
            session.removeAttribute("email");
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            // Đăng nhập thất bại
            request.setAttribute("error_message", "Email hoặc mật khẩu không đúng.");
            session.removeAttribute("email");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
        }
    }

}