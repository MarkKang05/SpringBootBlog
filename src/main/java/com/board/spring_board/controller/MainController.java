package com.board.spring_board.controller;

import com.board.spring_board.jwt.JwtUtils;
import com.board.spring_board.model.User;
import com.board.spring_board.service.BoardService;
import com.board.spring_board.utils.HttpSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HttpSession session;

    private final JwtUtils jwtUtils;

    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/")
    public String home(HttpServletRequest request, Model model){
//        if(HttpSessionUtils.isLoginUser(session)) {
//            User currentUser = (User) session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
//            model.addAttribute("username",currentUser.getUsername());
//        }

        Cookie accessToken = WebUtils.getCookie(request, "accessToken");
        if (accessToken != null) {
//            model.addAttribute("username", jwtUtils.getUserNameFromJwtToken(accessToken.getValue()));
            model.addAttribute("username", "mark");
            System.out.println(jwtUtils.getUserNameFromJwtToken(accessToken.getValue()));
        }


        model.addAttribute("boards", boardService.getAllBoard());

        return "index";
    }

}
