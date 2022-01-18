package com.board.spring_board.request;

import com.board.spring_board.dto.jwt.TokenDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestLogin {
    TokenDto tokenDto;
    String error;

    public RequestLogin(TokenDto tokenDto) {
        this.tokenDto = tokenDto;
    }

    public RequestLogin(String message) {
        this.error = message;
    }
}
