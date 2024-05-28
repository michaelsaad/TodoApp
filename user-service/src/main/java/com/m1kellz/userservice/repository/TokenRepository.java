package com.m1kellz.userservice.repository;

import com.m1kellz.userservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

  Token findByToken(String token);
}
