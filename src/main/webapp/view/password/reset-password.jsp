<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/password/reset-password.css" rel="stylesheet" />
</head>
<body>
<div class="container mt-5">
    <h3>Đặt lại mật khẩu</h3>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <form action="${pageContext.request.contextPath}/reset-password" method="post">
        <input type="hidden" name="token" value="${param.token}"> <!-- Token từ URL -->

        <div class="form-group">
            <label>Mật khẩu mới:</label>
            <input type="password" name="newPassword" class="form-control" required>
        </div>

        <div class="form-group">
            <label>Xác nhận mật khẩu:</label>
            <input type="password" name="confirmPassword" class="form-control" required>
        </div>

        <button type="submit" class="btn btn-success mt-3">Cập nhật mật khẩu</button>
    </form>
</div>
</body>
</html>
