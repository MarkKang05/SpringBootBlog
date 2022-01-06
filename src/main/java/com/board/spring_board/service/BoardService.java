package com.board.spring_board.service;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.dto.board.RequestUpdateBoardDto;
import com.board.spring_board.model.Board;
import com.board.spring_board.model.User;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    void create(RequestSaveBoardDto requestSaveBoardDto, User user);
    List<Board> getAllBoard();
    Board getBoard(Long id);

    void update(Long id, RequestUpdateBoardDto requestUpdateBoardDto);

    void deleteBoard(Long id);
    boolean hasAuthority(Long boardId, Long userId);
}
