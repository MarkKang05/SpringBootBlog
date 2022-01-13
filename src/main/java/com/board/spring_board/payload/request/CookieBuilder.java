package com.board.spring_board.payload.request;

import lombok.Builder;

import javax.servlet.http.Cookie;

public class CookieBuilder extends Cookie{

    public CookieBuilder(String name, String value) {
        super(name, value);
        super.setHttpOnly(true);
        super.setPath("/");
    }

    public CookieBuilder(String name, String value, int expired){
        super(name, value);
        super.setHttpOnly(true);
        super.setPath("/");
        super.setMaxAge(expired);
    }

}
