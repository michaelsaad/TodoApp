package com.m1kellz.todoservice.model;

import com.m1kellz.todoservice.entity.Priority;
import com.m1kellz.todoservice.entity.Status;

import java.time.LocalDateTime;

public record TodoResponse(
         Long id ,
         String title,
         String description,
         Priority priority,
         Status status,
         LocalDateTime createdAt,
         LocalDateTime lastUpdated
) {
}
