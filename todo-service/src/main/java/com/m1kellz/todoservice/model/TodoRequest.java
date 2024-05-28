package com.m1kellz.todoservice.model;

import com.m1kellz.todoservice.entity.Priority;
import com.m1kellz.todoservice.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "ToDo Model")
public record TodoRequest(
        @Schema(description = "Todo Title")
        @NotEmpty(message = "title Can not be empty ")
        String title,
        String description,
        @Schema(description = " pls insert one of these values: 'REAL' 'CRITICAL' 'IMPORTANT' 'ROUTINE' ")
        @Enumerated(EnumType.STRING)
        Priority priority,
        @Schema(description = " pls insert one of these values: 'FINISHED' 'UNFINISHED' 'IN_PROGRESS' ")
        @Enumerated(EnumType.STRING)
        Status status
) {

}