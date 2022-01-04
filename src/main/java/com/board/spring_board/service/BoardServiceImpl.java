package com.board.spring_board.service;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.model.Board;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public void create(RequestSaveBoardDto requestSaveBoardDto, User user) {
        requestSaveBoardDto.setUser(user);
        boardRepository.save(requestSaveBoardDto.toEntity());
    }

    @Override
    public List<Board> getAllBoard() {
//        List<Board> boards = boardRepository.findAllByCreatedDate();
        return boardRepository.findAll();
    }

    public Optional<Board> getBoard(Long id){
        return boardRepository.findById(id);
    }

}
