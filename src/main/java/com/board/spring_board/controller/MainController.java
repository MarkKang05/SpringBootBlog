package com.board.spring_board.controller;

import com.board.spring_board.model.User;
import com.board.spring_board.utils.HttpSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HttpSession session;

    @GetMapping(value = "/")
    public String home(Model model){
        if(HttpSessionUtils.isLoginUser(session)) {
            User currentUser = (User) session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
            model.addAttribute("username",currentUser.getUsername());
        }

        return "index";
    }

}
