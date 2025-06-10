package com.example.casestudymodule3.controller.auth;

import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.github.cdimascio.dotenv.Dotenv;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "GoogleCallbackServlet", urlPatterns = "/google-callback")
public class GoogleCallbackServlet extends HttpServlet {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String CLIENT_ID = dotenv.get("CLIENT_ID");
    private static final String CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
    private static final String REDIRECT_URI = dotenv.get("REDIRECT_URI");

    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login?error_message=Không nhận được code từ Google");
            return;
        }
        if (state == null || !state.equals(request.getSession().getAttribute("oauth_state"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid state parameter");
            return;
        }

        try {
            GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    REDIRECT_URI
            );
            GoogleTokenResponse tokenResponse = tokenRequest.execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            if (idToken == null) {
                throw new IOException("Không thể phân tích Id Token");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String googleId = payload.getSubject();

            // 1. Kiểm tra user trong DB
            User user = userService.findByEmail(email);
            if (user == null) {
                // Nếu chưa có user, tạo mới với googleId
                user = new User(0, name, email, "", "", googleId, "", "user", null);
                boolean saved = userService.save(user);
                if (!saved) {
                    response.sendRedirect(request.getContextPath() + "/login?error_message=Không thể lưu tài khoản Google mới.");
                    return;
                }
            } else {
                // Nếu user có rồi mà chưa lưu googleId, cập nhật
                if (user.getGoogleId() == null || user.getGoogleId().isEmpty()) {
                    user.setGoogleId(googleId);
                    boolean updated = userService.update(user);
                    if (!updated) {
                        response.sendRedirect(request.getContextPath() + "/login?error_message=Cập nhật tài khoản Google thất bại.");
                        return;
                    }
                }
            }

            // 2. Lưu user vào session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // 3. Chuyển hướng đến home
            response.sendRedirect(request.getContextPath() + "/home");

        } catch (IOException e) {
            response.sendRedirect(request.getContextPath() + "/login?error_message=Lỗi khi trao đổi token: " + e.getMessage());
        }
    }
}
