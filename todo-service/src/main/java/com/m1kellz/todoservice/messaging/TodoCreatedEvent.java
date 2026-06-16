package com.m1kellz.todoservice.messaging;

public record TodoCreatedEvent(int todoId, int userId, String title) {
}
