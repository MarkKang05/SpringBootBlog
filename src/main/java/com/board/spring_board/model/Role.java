package com.board.spring_board.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("USER", "사용자"), ADMIN("ADMIN_USER", "어드민");

    private final String key;
    private final String title;

}
