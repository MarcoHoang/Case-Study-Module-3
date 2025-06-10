package com.example.casestudymodule3.controller;

import com.example.casestudymodule3.dao.UserProfileDAO;
import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.model.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "HomeServlet", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {

    private final UserProfileDAO profileDAO = UserProfileDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        req.setAttribute("user", user);

        // ✅ Lấy profile theo userId và gửi qua JSP
        UserProfile profile = profileDAO.getProfileByUserId(user.getId());
        req.setAttribute("profile", profile);

        // Forward đến trang home.jsp
        req.getRequestDispatcher("view/home.jsp").forward(req, resp);
    }
}
