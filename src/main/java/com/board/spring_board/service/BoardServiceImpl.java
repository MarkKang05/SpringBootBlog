package com.board.spring_board.service;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService{

    private BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public void create(RequestSaveBoardDto requestSaveBoardDto, User user) {
        requestSaveBoardDto.setUser(user);
        boardRepository.save(requestSaveBoardDto.toEntity());
    }

}
