package com.m1kellz.userservice.model.request;

import com.m1kellz.userservice.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "New user registration payload")
public class RegisterRequest {

    @Schema(description = "First name", example = "Mickey")
    private String firstname;

    @Schema(description = "Last name", example = "Mouse")
    private String lastname;

    @Schema(description = "Unique email address", example = "mickey@test.com")
    private String email;

    @Schema(description = "Account password", example = "password123")
    private String password;

    @Schema(description = "User role", example = "USER", allowableValues = {"USER", "ADMIN", "MANAGER"})
    private Role role;
}
