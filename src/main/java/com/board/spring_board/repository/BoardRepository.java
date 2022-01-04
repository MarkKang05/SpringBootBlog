package com.board.spring_board.repository;

import com.board.spring_board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {
//    List<Board> findAllByCreatedDate();
}
