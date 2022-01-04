package com.board.spring_board.controller;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.model.Board;
import com.board.spring_board.service.BoardService;
import com.board.spring_board.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/boardForm")
    public String boardCreatePage(){
        return "board/boardCreate";
    }

    @PostMapping("/board/create")
    public String boardCreateProc(RequestSaveBoardDto requestSaveBoardDto, HttpSession session){
//        System.out.println(HttpSessionUtils.getUserFromSession(session).toString());
        boardService.create(requestSaveBoardDto, HttpSessionUtils.getUserFromSession(session));
        for(Board board : boardService.getAllBoard()){
            System.out.println(board.toString());
        }
        return "redirect:/";
    }

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable Long id, Model model){
        System.out.println(boardService.getBoard(id).get());
        model.addAttribute(boardService.getBoard(id).get());
        return "/board/boardDetail";
    }

}
