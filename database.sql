-- Tạo cơ sở dữ liệu user_registration
CREATE DATABASE user_registration;

-- Chọn cơ sở dữ liệu user_registration để sử dụng
USE user_registration;

-- Tạo bảng users để lưu trữ thông tin người dùng
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY, -- ID người dùng, tự động tăng và là khóa chính
                       name VARCHAR(100) NOT NULL, -- Tên người dùng, không thể để trống
                       email VARCHAR(100) UNIQUE, -- Email người dùng, phải duy nhất
                       phone VARCHAR(20) UNIQUE, -- Số điện thoại người dùng, phải duy nhất
                       password VARCHAR(255) DEFAULT NULL, -- Mật khẩu của người dùng, mặc định NULL nếu người dùng đăng nhập qua Google
                       google_id VARCHAR(255) UNIQUE, -- Google ID, duy nhất để phục vụ cho việc đăng nhập qua Google
                       verification_code VARCHAR(6), -- Mã xác minh 6 chữ số dùng để xác thực người dùng
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian tạo tài khoản, mặc định là thời gian hiện tại
                       last_login TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP, -- Thời gian đăng nhập cuối cùng, tự động cập nhật mỗi lần đăng nhập
                       role ENUM('USER', 'ADMIN') DEFAULT 'USER' -- Phân quyền cho người dùng, mặc định là USER
);

-- Tạo bảng user_profiles để lưu trữ thông tin chi tiết về người dùng (Profile)
CREATE TABLE user_profiles (
                               user_id INT PRIMARY KEY, -- Liên kết với bảng users thông qua user_id, đây là khóa chính của bảng này
                               full_name VARCHAR(100), -- Tên đầy đủ của người dùng
                               avatar_url VARCHAR(255), -- URL của ảnh đại diện của người dùng (có thể là một URL đến server hoặc dịch vụ lưu trữ ảnh)
                               dob DATE, -- Ngày sinh của người dùng
                               gender ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER', -- Giới tính của người dùng (Mặc định là 'OTHER' nếu không chọn)
                               bio TEXT, -- Mô tả về người dùng
                               address VARCHAR(255), -- Địa chỉ của người dùng
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Liên kết với bảng users, nếu người dùng bị xóa thì profile cũng bị xóa (ON DELETE CASCADE)
);

-- Tạo bảng password_reset_tokens để lưu trữ mã reset mật khẩu
CREATE TABLE password_reset_tokens (
                                       id INT AUTO_INCREMENT PRIMARY KEY, -- ID của token, tự động tăng và là khóa chính
                                       user_id INT, -- ID người dùng mà token này liên kết
                                       token VARCHAR(255) NOT NULL, -- Token dùng để reset mật khẩu
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian tạo token, mặc định là thời gian hiện tại
                                       expires_at TIMESTAMP, -- Thời gian hết hạn của token (ví dụ: sau 1 giờ)
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Liên kết với bảng users, nếu người dùng bị xóa thì token cũng bị xóa
);

-- Tạo index trên cột email của bảng users để tối ưu hóa truy vấn tìm kiếm theo email
CREATE INDEX idx_email ON users(email);

-- Tạo index trên cột google_id của bảng users để tối ưu hóa truy vấn tìm kiếm theo Google ID
CREATE INDEX idx_google_id ON users(google_id);
