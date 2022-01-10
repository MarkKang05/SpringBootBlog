package com.board.spring_board.jwt;

public interface JwtProperties {
    String SECRET = "test";
    int EXPIRATION_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
