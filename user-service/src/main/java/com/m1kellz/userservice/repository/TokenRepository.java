package com.m1kellz.userservice.repository;

import com.m1kellz.userservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

  Token findByToken(String token);
}
