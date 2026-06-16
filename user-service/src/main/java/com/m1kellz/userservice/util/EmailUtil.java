package com.m1kellz.userservice.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        if (!mailEnabled) {
            log.info("DEV MODE - OTP for {}: {}", email, otp);
            return;
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("The OTP for email " + email + " is: " + otp);
        javaMailSender.send(mimeMessage);
    }

    public void sendSetPassword(String email) throws MessagingException {
        if (!mailEnabled) {
            log.info("DEV MODE - Password reset link for {}: http://localhost:8083/api/v1/auth/reset-password/{}", email, email);
            return;
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password");
        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:8083/api/v1/auth/reset-password/%s" target="_blank">Click link to reset password</a>
        </div>
        """.formatted(email), true);
        javaMailSender.send(mimeMessage);
    }
}
