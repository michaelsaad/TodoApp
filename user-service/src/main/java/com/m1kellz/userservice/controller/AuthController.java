package com.m1kellz.userservice.controller;


import com.m1kellz.userservice.model.request.AuthenticationResponse;
import com.m1kellz.userservice.model.request.LoginRequest;
import com.m1kellz.userservice.model.request.RegisterRequest;
import com.m1kellz.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/rest/auth")
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


    @GetMapping("activate/{email}")
    public ResponseEntity<Void> updateTodo(@PathVariable String email,
                                           @RequestHeader("OTP") String otp) {



            return ResponseEntity.ok().build();


    }

}