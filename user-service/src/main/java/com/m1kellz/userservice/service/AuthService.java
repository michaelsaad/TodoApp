package com.m1kellz.userservice.service;

import com.m1kellz.userservice.entity.TokenType;
import com.m1kellz.userservice.entity.Token;
import com.m1kellz.userservice.entity.User;
import com.m1kellz.userservice.model.request.AuthenticationResponse;
import com.m1kellz.userservice.model.request.LoginRequest;
import com.m1kellz.userservice.model.request.RegisterRequest;
import com.m1kellz.userservice.repository.TokenRepository;
import com.m1kellz.userservice.repository.UserRepository;
import com.m1kellz.userservice.util.EmailUtil;
import com.m1kellz.userservice.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService
{
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private  AuthenticationManager authenticationManager;

    public AuthenticationResponse login(LoginRequest request)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found with this email: " + request.getEmail()));
        Map<String , Object> extraClaims = new HashMap<>();
        String jwtToken = jwtService.createToken(user , extraClaims);
        saveUserToken(user, jwtToken);
        if (!user.getVerified()) {
            throw new RuntimeException("Account is not verified");
        }
        return new AuthenticationResponse(jwtToken , request.getEmail());
    }

    public AuthenticationResponse register(RegisterRequest request)
    {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(request.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .otp(otp)
                .otpGeneratedTime(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        Map<String , Object> extraClaims = new HashMap<>();
        String jwtToken = jwtService.createToken(user , extraClaims);
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponse(jwtToken , request.getEmail());
    }

    public String verifyAccount(String email, String otp) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setVerified(true);
            userRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again";
    }

    public String regenerateOtp(String email) {
        User user = userRepository.findUserByEmail((email)).orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public String forgotPassword(String email) {
        User user = userRepository.findUserByEmail((email)).
                orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        try {
            emailUtil.sendSetPassword(email);

        }catch (MessagingException e){
            throw new RuntimeException("unable to send reset password mail ");
        }
        return "please check your mail";
    }

    public String setPassword(String email, String password) {
        User user = userRepository.findUserByEmail((email)).
                orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "your password changed !";

    }
}
