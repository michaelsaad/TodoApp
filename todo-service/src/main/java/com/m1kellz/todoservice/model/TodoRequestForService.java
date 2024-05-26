package com.m1kellz.todoservice.model;


import com.m1kellz.todoservice.entity.Priority;
import com.m1kellz.todoservice.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ToDo Model")
public record TodoRequestForService(
        @Schema(description = "Todo Title")
        String title,
        Long userId ,
        String description,
        @Schema(description = " pls insert one of these values: 'REAL' 'CRITICAL' 'IMPORTANT' 'ROUTINE' ")
        Priority priority,
        @Schema(description = " pls insert one of these values: 'FINISHED' 'UNFINISHED' 'IN_PROGRESS' ")
        Status status
        ) {
}
