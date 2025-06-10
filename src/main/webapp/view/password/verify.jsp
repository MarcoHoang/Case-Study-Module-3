<%--
  Created by IntelliJ IDEA.
  User: Hoang Tran
  Date: 02/06/2025
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực OTP</title>
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/password/verify.css"> <!-- Liên kết với file
    CSS của
     bạn
     -->
</head>
<body>
<div class="container">
    <h2>Xác thực OTP</h2>

    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty error}">
        <div class="error-message show">
                ${error}
        </div>
    </c:if>

    <!-- Form nhập OTP -->
    <form action="${pageContext.request.contextPath}/verify-otp" method="POST">
        <div class="form-group">
            <label for="otp">Nhập mã OTP</label>
            <input type="text" id="otp" name="otp" class="form-control" placeholder="Mã OTP" required>
        </div>

        <button type="submit" class="btn btn-primary">Xác nhận OTP</button>
    </form>

    <!-- Liên kết gửi lại OTP -->
    <p class="text-center mt-3">
        Không nhận được mã OTP? <a href="${pageContext.request.contextPath}/resendOtp">Gửi lại OTP</a>
    </p>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>



