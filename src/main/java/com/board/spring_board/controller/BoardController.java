package com.board.spring_board.controller;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.service.BoardService;
import com.board.spring_board.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        System.out.println(HttpSessionUtils.getUserFromSession(session).toString());
        boardService.create(requestSaveBoardDto, HttpSessionUtils.getUserFromSession(session));
        return "redirect:/";
    }

}
