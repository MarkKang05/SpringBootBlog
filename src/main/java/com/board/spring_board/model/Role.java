package com.board.spring_board.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "사용자");

    private final String key;
    private final String title;

}
