<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Hồ sơ người dùng</title>

    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/css/profile/profile.css" rel="stylesheet" />
</head>
<body>
<div class="card-container">
    <h2>Cập nhật hồ sơ</h2>

    <c:if test="${not empty message}">
        <div class="success-message" style="color:green; margin-bottom: 15px;">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error-message show">${error}</div>
    </c:if>

    <form method="post" action="" enctype="multipart/form-data" novalidate>
        <div class="form-group" style="position:relative;">
            <input type="text" id="fullName" name="fullName" class="form-control"
                   value="${profile.fullName}"
                   required
                   placeholder="Nhập họ và tên đầy đủ"
                   title="Nhập họ và tên của bạn"
                   aria-label="Họ tên" />
            <label for="fullName">Họ tên</label>
        </div>

        <div class="form-group" style="position:relative;">
            <input type="file" id="avatarFile" name="avatarFile" class="form-control" accept="image/*"
                   title="Chọn ảnh đại diện (jpg, png, ...)"
                   aria-label="Ảnh đại diện" />
            <label for="avatarFile">Ảnh đại diện</label>
            <c:if test="${not empty profile.avatarUrl}">
                <img src="${profile.avatarUrl}" alt="Avatar hiện tại" style="max-width: 150px; margin-top: 10px;" />
            </c:if>
        </div>

        <div class="form-group" style="position:relative;">
            <input type="date" id="dob" name="dob" class="form-control"
                   value="${profile.dob}"
                   placeholder="Chọn ngày sinh"
                   title="Chọn ngày sinh của bạn"
                   aria-label="Ngày sinh" />
            <label for="dob">Ngày sinh</label>
        </div>

        <div class="form-group" style="position:relative;">
            <select id="gender" name="gender" class="form-control" required
                    title="Chọn giới tính của bạn"
                    aria-label="Giới tính">
                <option value="" disabled ${profile.gender == null ? 'selected' : ''}>-- Chọn giới tính --</option>
                <option value="MALE" ${profile.gender == 'MALE' ? 'selected' : ''}>Nam</option>
                <option value="FEMALE" ${profile.gender == 'FEMALE' ? 'selected' : ''}>Nữ</option>
                <option value="OTHER" ${profile.gender == 'OTHER' ? 'selected' : ''}>Khác</option>
            </select>
            <label for="gender">Giới tính</label>
        </div>

        <div class="form-group" style="position:relative;">
        <textarea id="bio" name="bio" rows="5" class="form-control"
                  placeholder="Viết vài dòng giới thiệu về bản thân"
                  title="Mô tả về bạn"
                  aria-label="Mô tả">${profile.bio}</textarea>
            <label for="bio">Mô tả</label>
        </div>

        <div class="form-group" style="position:relative;">
            <input type="text" id="address" name="address" class="form-control"
                   value="${profile.address}"
                   placeholder="Nhập địa chỉ nơi bạn sinh sống"
                   title="Địa chỉ hiện tại của bạn"
                   aria-label="Địa chỉ" />
            <label for="address">Địa chỉ</label>
        </div>

        <button type="submit" class="btn-primary">Cập nhật hồ sơ</button>
    </form>

</div>
</body>
</html>
