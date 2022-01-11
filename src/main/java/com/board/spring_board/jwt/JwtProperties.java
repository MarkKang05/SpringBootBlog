package com.board.spring_board.jwt;

public interface JwtProperties {
    String SECRET = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
    int EXPIRATION_TIME = 60000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
