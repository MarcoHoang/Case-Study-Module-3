<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Trang chủ</title>

  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" />
  <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet" />
</head>
<body>
<div class="container mt-5">
  <h2 class="mb-4 text-center">Chào mừng bạn đến với trang chủ!</h2>

  <c:choose>
    <c:when test="${not empty user}">
      <div class="card mx-auto" style="max-width: 600px;">
        <div class="card-body">
          <h5 class="card-title">Thông tin tài khoản</h5>
          <p class="card-text"><strong>Email:</strong> ${user.email}</p>
          <p class="card-text"><strong>Số điện thoại:</strong> ${user.phone}</p>
          <p class="card-text"><strong>Vai trò:</strong> ${user.role}</p>

          <c:if test="${not empty user.googleId}">
            <p class="card-text"><strong>Đăng nhập qua Google:</strong> Có</p>
          </c:if>

          <!-- Nút bật thông tin đầy đủ -->
          <button class="btn btn-info w-100 mt-3" type="button" data-bs-toggle="collapse" data-bs-target="#profileDetail">
            Thông tin đầy đủ
          </button>

          <!-- Thông tin hồ sơ (ẩn mặc định, hiện khi click nút) -->
          <div class="collapse mt-3" id="profileDetail">
            <hr>
            <h5 class="card-title">Thông tin hồ sơ</h5>

            <c:if test="${not empty profile}">
              <p class="card-text"><strong>Họ và tên:</strong> ${profile.fullName}</p>
              <p class="card-text"><strong>Ngày sinh:</strong> ${profile.dob}</p>
              <p class="card-text"><strong>Giới tính:</strong> ${profile.gender}</p>
              <p class="card-text"><strong>Địa chỉ:</strong> ${profile.address}</p>
              <p class="card-text"><strong>Giới thiệu:</strong> ${profile.bio}</p>
              <c:if test="${not empty profile.avatarUrl}">
                <p class="card-text"><strong>Ảnh đại diện:</strong></p>
                <img src="${profile.avatarUrl}" alt="Avatar" class="img-fluid rounded" style="max-width: 150px;">
              </c:if>
            </c:if>

            <c:if test="${empty profile}">
              <p class="text-muted">Bạn chưa cập nhật hồ sơ cá nhân.</p>
            </c:if>
          </div>

          <!-- Nút cập nhật & đăng xuất -->
          <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary w-100 mt-3">Cập nhật thông tin</a>
          <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger w-100 mt-2">Đăng xuất</a>
        </div>
      </div>
    </c:when>

    <c:otherwise>
      <div class="alert alert-warning text-center" role="alert">
        Vui lòng <a href="${pageContext.request.contextPath}/login" class="alert-link">đăng nhập</a> để xem thông tin.
      </div>
    </c:otherwise>
  </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
