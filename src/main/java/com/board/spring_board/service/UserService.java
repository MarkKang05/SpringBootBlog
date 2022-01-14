package com.board.spring_board.service;

import com.board.spring_board.dto.user.RequestSaveUserDto;

public interface UserService {
    void createUser(RequestSaveUserDto requestSaveUserDto);

    Long getIdByUsername(String username);
}
