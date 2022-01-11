package com.board.spring_board.payload.response;

import lombok.Getter;

@Getter
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String refreshToken, String accessToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
