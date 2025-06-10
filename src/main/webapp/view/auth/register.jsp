<%--
  Created by IntelliJ IDEA.
  User: Hoang Tran
  Date: 28/05/2025
  Time: 22:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/auth/register.css" rel="stylesheet">
</head>
<body>
<div class="register-container">
    <h2>Đăng ký</h2>
    <!-- Hiển thị thông báo lỗi từ Servlet -->
    <c:if test="${not empty error_message}">
        <div class="error-message show">
                ${error_message}
        </div>
    </c:if>
    <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateForm(event)">
        <div class="form-group">
            <label for="name" class="form-label">Họ và tên:</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="Tran Huy Hoang" required>
        </div>
        <div class="form-group">
            <label for="email" class="form-label">Email:</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="hoang1111@gmail.com" required>
            <div id="emailError" class="error-message">
                Vui lòng nhập địa chỉ email hợp lệ.
            </div>
        </div>
        <div class="form-group">
            <label for="phone" class="form-label">Số điện thoại:</label>
            <input type="text" class="form-control" id="phone" name="phone" placeholder="0828686888" required>
            <div id="phoneError" class="error-message">
                Vui lòng nhập số điện thoại hợp lệ.
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="form-label">Mật khẩu:</label>
            <input type="password" class="form-control" id="password" name="password" required>
            <div id="passwordError" class="error-message">
                Mật khẩu phải có ít nhất 6 ký tự.
            </div>
        </div>
        <div class="form-group">
            <label for="confirmPassword" class="form-label">Xác nhận mật khẩu:</label>
            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
            <div id="confirmPasswordError" class="error-message">
                Mật khẩu và xác nhận mật khẩu không khớp.
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Đăng ký</button>
    </form>
    <p class="text-center">Đã có tài khoản? <a href="${pageContext.request.contextPath}/view/auth/login.jsp">Đăng
        nhập</a></p>
</div>
<script>
    function validateForm(event) {
        event.preventDefault();
        const email = document.getElementById('email').value;
        const phone = document.getElementById('phone').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const emailError = document.getElementById('emailError');
        const phoneError = document.getElementById('phoneError');
        const passwordError = document.getElementById('passwordError');
        const confirmPasswordError = document.getElementById('confirmPasswordError');
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const phoneRegex = /^\d{10,11}$/;

        let isValid = true;

        // Validate email
        if (!emailRegex.test(email)) {
            document.getElementById('email').classList.add('error');
            emailError.classList.add('show');
            isValid = false;
        } else {
            document.getElementById('email').classList.remove('error');
            emailError.classList.remove('show');
        }

        // Validate phone
        if (!phoneRegex.test(phone)) {
            document.getElementById('phone').classList.add('error');
            phoneError.classList.add('show');
            isValid = false;
        } else {
            document.getElementById('phone').classList.remove('error');
            phoneError.classList.remove('show');
        }

        // Validate password length
        if (password.length < 6) {
            document.getElementById('password').classList.add('error');
            passwordError.classList.add('show');
            isValid = false;
        } else {
            document.getElementById('password').classList.remove('error');
            passwordError.classList.remove('show');
        }

        // Validate confirm password
        if (password !== confirmPassword) {
            document.getElementById('confirmPassword').classList.add('error');
            confirmPasswordError.classList.add('show');
            isValid = false;
        } else {
            document.getElementById('confirmPassword').classList.remove('error');
            confirmPasswordError.classList.remove('show');
        }

        if (isValid) {
            document.getElementById('registerForm').submit();
        }
        return isValid;
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>