package com.board.spring_board.controller;

import com.board.spring_board.dto.board.RequestSaveBoardDto;
import com.board.spring_board.dto.board.RequestUpdateBoardDto;
import com.board.spring_board.model.Board;
import com.board.spring_board.model.User;
import com.board.spring_board.service.BoardService;
import com.board.spring_board.utils.HttpSessionUtils;
import com.board.spring_board.utils.SecurityUtils;
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
    @Autowired
    private SecurityUtils securityUtils;

    @GetMapping("/board/boardForm")
    public String boardCreatePage(){
        if (!securityUtils.isLogin()){
            return "redirect:/";
        }
        return "board/boardCreate";
    }

    @PostMapping("/board/create")
    public String boardCreateProc(RequestSaveBoardDto requestSaveBoardDto){
//        System.out.println(HttpSessionUtils.getUserFromSession(session).toString());
        boardService.create(requestSaveBoardDto, securityUtils.getUserFromSecurity());
//        for(Board board : boardService.getAllBoard()){
//            System.out.println(board.toString());
//        }
        return "redirect:/";
    }

    @GetMapping("/board/details/{id}")
    public String getBoard(@PathVariable Long id, Model model){

//        System.out.println(boardService.getBoard(id).get());
        model.addAttribute(boardService.getBoard(id));
        return "/board/boardDetail";
    }

    @GetMapping("/board/update/{id}")
    public String boardUpdatePage(@PathVariable Long id, Model model, HttpSession session){
        Long userId = ((User) session.getAttribute(HttpSessionUtils.USER_SESSION_KEY)).getId();
        if (!boardService.hasAuthority(id, userId)){
            return "redirect:/";
        }

        model.addAttribute("board", boardService.getBoard(id));

        return "/board/boardUpdate";
    }

    @PostMapping("/board/updateProc/{id}")
    public String boardUpdateProc(@PathVariable Long id, RequestUpdateBoardDto requestUpdateBoardDto){
        boardService.update(id, requestUpdateBoardDto);

        return "redirect:/";
    }

    @GetMapping("/board/delete/{id}")
    public String boardDeleteProc(@PathVariable Long id, HttpSession session){
        Long userId = ((User) session.getAttribute(HttpSessionUtils.USER_SESSION_KEY)).getId();
        if (!boardService.hasAuthority(id, userId)){
            return "redirect:/";
        }

        boardService.deleteBoard(id);
        return "redirect:/";
    }

}
