package com.board.spring_board.service;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.model.User;

public interface BoardService {
    void create(RequestSaveBoardDto requestSaveBoardDto, User user);
}
