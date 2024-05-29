package com.m1kellz.userservice.controller;


import com.m1kellz.userservice.model.request.AuthenticationResponse;
import com.m1kellz.userservice.model.request.LoginRequest;
import com.m1kellz.userservice.model.request.RegisterRequest;
import com.m1kellz.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


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
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(authService.regenerateOtp(email), HttpStatus.OK);
    }

}