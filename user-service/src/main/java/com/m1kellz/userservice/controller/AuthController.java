package com.m1kellz.userservice.controller;

import com.m1kellz.userservice.auth.CheckToken;
import com.m1kellz.userservice.model.request.AuthenticationResponse;
import com.m1kellz.userservice.model.request.LoginRequest;
import com.m1kellz.userservice.model.request.RegisterRequest;
import com.m1kellz.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Authentication", description = "Register, verify, login, and token validation")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private CheckToken checkToken;

    @Operation(summary = "Login", description = "Authenticate with email and password. Account must be verified first.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or account not verified")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "Register", description = "Create account and queue OTP email. Check user-service console for OTP when mail is disabled.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created; OTP sent", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Registration failed")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @Operation(summary = "Activate account", description = "Verify email with OTP received after registration (valid for 60 seconds).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account activated"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    @PutMapping("/activate/{email}")
    public ResponseEntity<String> verifyAccount(
            @Parameter(description = "User email address", example = "mickey@test.com") @PathVariable String email,
            @Parameter(description = "6-digit OTP from email or console", example = "123456") @RequestHeader("OTP") String otp) {
        return new ResponseEntity<>(authService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @Operation(summary = "Regenerate OTP", description = "Send a new OTP if the previous one expired.")
    @PutMapping("/regenerate-otp/{email}")
    public ResponseEntity<String> regenerateOtp(
            @Parameter(description = "User email address", example = "mickey@test.com") @PathVariable String email) {
        return new ResponseEntity<>(authService.regenerateOtp(email), HttpStatus.OK);
    }

    @Operation(summary = "Forgot password", description = "Send password reset instructions to the user's email.")
    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(
            @Parameter(description = "User email address", example = "mickey@test.com") @PathVariable String email) {
        return new ResponseEntity<>(authService.forgotPassword(email), HttpStatus.OK);
    }

    @Operation(summary = "Reset password", description = "Set a new password using the reset flow.")
    @PutMapping("/reset-password/{email}")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "User email address", example = "mickey@test.com") @PathVariable String email,
            @Parameter(description = "New password", example = "newPassword123") @RequestHeader("password") String password) {
        return new ResponseEntity<>(authService.setPassword(email, password), HttpStatus.OK);
    }

    @Operation(summary = "Validate JWT", description = "Used by todo-service to verify a bearer token. Returns true if valid.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> checkToken(
            @Parameter(description = "Bearer JWT", example = "Bearer eyJhbGciOiJIUzI1NiJ9...") @RequestHeader("Authorization") String token) {
        boolean result = checkToken.goodToken(token);
        if (result) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
