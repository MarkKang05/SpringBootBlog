package com.board.spring_board.jwt;

public interface JwtProperties {
    String SECRET = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
    int EXPIRATION_TIME = 60000;
    Long REFRESHTOKEN_EXPIRATION_TIME = 120000L;
}
