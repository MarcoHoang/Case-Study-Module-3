package com.example.casestudymodule3.util.email;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Properties;

public class EmailUtil {

    private static final Dotenv dotenv = Dotenv.load();

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    // Địa chỉ email và mật khẩu hoặc mật khẩu ứng dụng của bạn
    private static final String FROM_EMAIL = dotenv.get("FROM_EMAIL");  // Địa chỉ email của bạn
    private static final String EMAIL_PASSWORD = dotenv.get("EMAIL_PASSWORD");  // Mật khẩu hoặc mật khẩu ứng dụng

    // SMTP server host
    private static final String SMTP_HOST = dotenv.get("SMTP_HOST"); // SMTP server

    // Cấu hình các thuộc tính cho việc gửi email
    private static Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        return properties;
    }

    // Tạo Session với Authenticator
    private static Session createSession() {
        Properties properties = getMailProperties();

        // Sử dụng jakarta.mail.Authenticator thay vì java.net.Authenticator
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Chuyển mật khẩu thành mảng char[] để bảo mật hơn
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
    }

    // Gửi email và trả về true nếu thành công, false nếu có lỗi
    public static boolean sendEmail(String to, String subject, String body) {
        try {
            // Tạo Session
            Session session = createSession();

            // Tạo đối tượng MimeMessage để gửi email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            // Gửi email
            Transport.send(message);
            logger.info("Email sent successfully to {}", to);

            // Nếu gửi thành công, trả về true
            return true;

        } catch (MessagingException mex) {
            logger.error("Failed to send email to {}: {}", to, mex.getMessage(), mex);
        }

        // Nếu có lỗi, trả về false
        return false;
    }
}
