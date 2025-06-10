<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/password/forgot-password.css" rel="stylesheet" />
</head>
<body>
<div class="container mt-5">
    <h3>Quên mật khẩu</h3>

    <!-- Nếu email đã được gửi thành công, chỉ hiển thị thông báo -->
    <c:if test="${not empty message}">
        <div class="alert alert-info">${message}</div>
    </c:if>

    <!-- Nếu chưa gửi email, hiển thị form nhập email -->
    <c:if test="${empty isEmailSent}">
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
            <div class="form-group">
                <label for="email">Nhập email để lấy lại mật khẩu:</label>
                <input type="email" id="email" name="email" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary mt-3">Gửi link khôi phục</button>
        </form>
    </c:if>
</div>
</body>
</html>
