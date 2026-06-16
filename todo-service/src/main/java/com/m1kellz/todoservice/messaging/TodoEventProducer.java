package com.m1kellz.todoservice.messaging;

import com.m1kellz.todoservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class TodoEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public TodoEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTodoCreated(TodoCreatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TODO_EVENTS_QUEUE, event);
    }
}
