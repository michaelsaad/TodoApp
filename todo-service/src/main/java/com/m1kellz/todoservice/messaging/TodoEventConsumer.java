package com.m1kellz.todoservice.messaging;

import com.m1kellz.todoservice.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class TodoEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(TodoEventConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.TODO_EVENTS_QUEUE)
    public void handleTodoCreated(TodoCreatedEvent event) {
        log.info("Todo created event received: id={}, userId={}, title={}",
                event.todoId(), event.userId(), event.title());
    }
}
