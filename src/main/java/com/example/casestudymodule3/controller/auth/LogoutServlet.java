package com.example.casestudymodule3.controller.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xoá session hiện tại
        HttpSession session = request.getSession(false); // false để không tạo session mới nếu chưa có
        if (session != null) {
            session.invalidate(); // Xoá tất cả thuộc tính trong session
        }

        // Quay về trang login
        response.sendRedirect(request.getContextPath() + "/view/auth/login.jsp");
    }
}
