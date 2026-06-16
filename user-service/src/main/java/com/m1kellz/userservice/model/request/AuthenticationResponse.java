package com.m1kellz.userservice.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT authentication response")
public class AuthenticationResponse {

    @Schema(description = "JWT access token — use as Bearer token on todo-service", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Authenticated user email", example = "mickey@test.com")
    private String email;
}
