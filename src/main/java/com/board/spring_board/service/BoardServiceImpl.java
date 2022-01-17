package com.board.spring_board.service;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.dto.board.RequestUpdateBoardDto;
import com.board.spring_board.model.Board;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.BoardRepository;
import com.board.spring_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

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

    public Board getBoard(Long id){
        if(boardRepository.findById(id).isPresent()){
            return boardRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public void update(Long id, RequestUpdateBoardDto requestUpdateBoardDto) {
        Board board = boardRepository.findById(id).get();
        board.setTitle(requestUpdateBoardDto.getTitle());
        board.setDescription(requestUpdateBoardDto.getDescription());
        boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id).get();
        boardRepository.delete(board);
    }

    @Override
    public boolean hasAuthority(Long boardId, Long userId) {
        Board board = this.getBoard(boardId);
        User user = userRepository.findById(userId).get();
        return ( userId.equals(board.getUser().getId()) || user.getRole().equals(Role.ROLE_ADMIN));
    }

}
