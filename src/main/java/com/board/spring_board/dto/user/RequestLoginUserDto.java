package com.board.spring_board.dto.user;

import lombok.Data;

@Data
public class RequestLoginUserDto {
    private String email;
    private String password;
}
