package com.m1kellz.userservice.messaging;

import com.m1kellz.userservice.config.RabbitMQConfig;
import com.m1kellz.userservice.util.EmailUtil;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    private final EmailUtil emailUtil;

    public EmailConsumer(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @RabbitListener(queues = RabbitMQConfig.OTP_EMAIL_QUEUE)
    public void handleOtpEmail(OtpEmailMessage message) {
        try {
            emailUtil.sendOtpEmail(message.email(), message.otp());
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}", message.email(), e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}
