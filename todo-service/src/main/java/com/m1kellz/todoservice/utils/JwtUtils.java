package com.m1kellz.todoservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JwtUtils {
    private final String secretKey = "BigProblemMickey";
    private final String authServiceUrl = "http://your-auth-service-url";

    @Autowired
    private RestTemplate restTemplate;

    public boolean isTokenValid(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                authServiceUrl + "/validate-token", // Endpoint  authentication service
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean isTokenNotExpired(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                authServiceUrl + "/expiration-check", // Endpoint in authentication service
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getStatusCode().is2xxSuccessful();
    }

    public Long extractUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return Long.parseLong(claims.get("id").toString());
        } catch (Exception e) {
            return null;
        }
    }
}
