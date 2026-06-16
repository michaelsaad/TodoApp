package com.m1kellz.todoservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private final String secretKey;
    private final String authServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public JwtUtils(
            @Value("${app.jwt.secret}") String secretKey,
            @Value("${app.auth-service.url}") String authServiceUrl) {
        this.secretKey = secretKey;
        this.authServiceUrl = authServiceUrl;
    }

    @Cacheable(value = "tokenValidation", key = "#token", unless = "#result == false")
    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token.startsWith("Bearer ") ? token : "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    authServiceUrl + "/validate-token",
                    HttpMethod.GET,
                    entity,
                    Boolean.class
            );

            return response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody());
        } catch (RestClientException e) {
            return false;
        }
    }

    public Integer extractUserIdFromToken(String token) {
        try {
            String accessToken = token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
            Object idClaim = claims.get("id");
            if (idClaim == null) {
                return null;
            }
            return Integer.parseInt(idClaim.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
