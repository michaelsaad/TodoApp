package com.m1kellz.userservice.auth;

import com.m1kellz.userservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CheckToken {


    // check token via rest template
    @Autowired
    private JwtService jwtService ;
    @Autowired
    private UserDetailsService userDetailsService;

    public Boolean goodToken (String token){

        String accessToken = token.substring("Bearer ".length());
        Claims claims = jwtService.parseJwtClaims(accessToken);
        String userEmail = claims.getSubject();


        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            return userDetails != null && jwtService.isTokenValid(accessToken, userDetails);
        }

        return false;
    }
}
