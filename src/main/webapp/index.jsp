<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chào mừng đến với Google Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="welcome-section">
        <h1>Chào mừng đến với Dự án Google Login</h1>
        <p>Khám phá trải nghiệm đăng nhập an toàn và nhanh chóng với Google OAuth. Hãy bắt đầu ngay!</p>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-google">
            <img src="https://developers.google.com/identity/images/btn_google_signin_light_normal_web.png" alt="Google Sign-In" class="google-icon">
            Đăng nhập bằng Google
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>