package com.board.spring_board.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_USER("USER", "사용자"), ROLE_ADMIN("ADMIN_USER", "어드민");

    private final String key;
    private final String title;

}
