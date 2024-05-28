package com.m1kellz.todoservice.model;

import com.m1kellz.todoservice.entity.Priority;
import com.m1kellz.todoservice.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record TodoResponse(
         Long id ,
         String title,
         Long userId,
         String description,
         @Enumerated(EnumType.STRING)
         Priority priority,
         @Enumerated(EnumType.STRING)
         Status status,
         LocalDateTime createdAt,
         LocalDateTime lastUpdated
) {
}
