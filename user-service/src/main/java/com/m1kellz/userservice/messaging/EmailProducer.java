package com.m1kellz.userservice.messaging;

import com.m1kellz.userservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOtpEmail(String email, String otp) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.OTP_EMAIL_QUEUE, new OtpEmailMessage(email, otp));
    }
}
