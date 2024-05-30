package com.m1kellz.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "JWT")
public class Token
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;
  @Column(name = "token")
  public String token;
  @Column(name = "token_type")
  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;
  @Column(name = "revoked")
  public boolean revoked;
  @Column(name = "expired")
  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;
}
