package com.board.spring_board.dto.jwt;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
