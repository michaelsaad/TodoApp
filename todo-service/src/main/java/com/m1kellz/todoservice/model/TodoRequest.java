package com.m1kellz.todoservice.model;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ToDo Model")
public record TodoRequest(
        @Schema(description = "Todo Title")
        String title,
        Long userId ,
        String description,
        @Schema(description = " pls insert one of these values: 'REAL' 'CRITICAL' 'IMPORTANT' 'ROUTINE' ")
        String priority,
        @Schema(description = " pls insert one of these values: 'FINISHED' 'UNFINISHED' 'IN_PROGRESS' ")
        String status
        ) {
}
