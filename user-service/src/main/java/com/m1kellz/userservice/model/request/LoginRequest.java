package com.m1kellz.userservice.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Login credentials")
public class LoginRequest {

    @Schema(description = "Registered email", example = "mickey@test.com")
    private String email;

    @Schema(description = "Account password", example = "password123")
    private String password;
}
