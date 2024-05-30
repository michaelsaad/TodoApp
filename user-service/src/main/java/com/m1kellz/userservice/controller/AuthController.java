package com.m1kellz.userservice.controller;


import com.m1kellz.userservice.auth.CheckToken;
import com.m1kellz.userservice.model.request.AuthenticationResponse;
import com.m1kellz.userservice.model.request.LoginRequest;
import com.m1kellz.userservice.model.request.RegisterRequest;
import com.m1kellz.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private CheckToken checkToken;


    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest , @RequestParam Map<String , Object> claims)  {
       return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PutMapping("activate/{email}")
    public ResponseEntity<String> verifyAccount(@PathVariable String email,
                                                @RequestHeader("OTP") String otp) {
        return new ResponseEntity<>(authService.verifyAccount(email, otp), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp/{email}")
    public ResponseEntity<String> regenerateOtp(@PathVariable String email) {
        return new ResponseEntity<>(authService.regenerateOtp(email), HttpStatus.OK);
    }@PutMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        return new ResponseEntity<>(authService.regenerateOtp(email), HttpStatus.OK);
    }@PutMapping("/reset-password/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email,@RequestHeader("password") String password) {
        return new ResponseEntity<>(authService.setPassword(email ,password), HttpStatus.OK);
    }
    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> checkToken(@RequestHeader("Authorization") String token) {
        boolean result = checkToken.goodToken(token);
        if (result) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

}