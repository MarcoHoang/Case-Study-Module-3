package com.example.casestudymodule3.controller.auth;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import io.github.cdimascio.dotenv.Dotenv;

@WebServlet(name = "LoginGoogleServlet", urlPatterns = "/login-google")
public class LoginGoogleServlet extends HttpServlet {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String CLIENT_ID = dotenv.get("CLIENT_ID");
    private static final String REDIRECT_URI = dotenv.get("REDIRECT_URI");
    private static final String SCOPE = dotenv.get("SCOPE");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute("oauth_state", state);

        AuthorizationCodeRequestUrl authUrl = new AuthorizationCodeRequestUrl(
                "https://accounts.google.com/o/oauth2/v2/auth", CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .setScopes(Arrays.asList(SCOPE.split(" ")))
                .setResponseTypes(Collections.singletonList("code"))
                .set("access_type", "online") // hoặc "offline" nếu cần refresh token
                .set("state", state);

        response.sendRedirect(authUrl.build());
    }
}
