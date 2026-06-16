package com.m1kellz.todoservice.model;

import com.m1kellz.todoservice.entity.Priority;
import com.m1kellz.todoservice.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Todo with details for a user")
public record TodoResponse(
        @Schema(description = "Todo ID", example = "1")
        int id,
        @Schema(description = "Todo title", example = "Finish microservices project")
        String title,
        @Schema(description = "Owner user ID", example = "1")
        int userId,
        @Schema(description = "Detailed description", example = "Wire Eureka, config server, and Swagger")
        String description,
        @Schema(description = "Priority level", example = "IMPORTANT")
        Priority priority,
        @Schema(description = "Current status", example = "IN_PROGRESS")
        Status status,
        @Schema(description = "Created timestamp")
        LocalDateTime createdAt,
        @Schema(description = "Last updated timestamp")
        LocalDateTime lastUpdated
) {
}
