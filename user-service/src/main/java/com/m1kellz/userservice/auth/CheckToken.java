package com.m1kellz.userservice.auth;

import com.m1kellz.userservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Component;

@Component
public class CheckToken {


    // check token via rest template
    @Autowired
    private JwtService jwtService ;
    @Autowired
    private UserDetailsService userDetailsService;

    @Cacheable(value = "tokenValidation", key = "#token", unless = "#result == false")
    public Boolean goodToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        try {
            String accessToken = token.substring("Bearer ".length());
            Claims claims = jwtService.parseJwtClaims(accessToken);
            String userEmail = claims.getSubject();

            if (userEmail == null) {
                return false;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            return jwtService.isTokenValid(accessToken, userDetails);
        } catch (Exception e) {
            return false;
        }
    }
}
