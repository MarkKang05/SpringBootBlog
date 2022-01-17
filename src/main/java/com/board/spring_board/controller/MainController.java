package com.board.spring_board.controller;

import com.board.spring_board.service.BoardService;
import com.board.spring_board.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/")
    public String home(Model model){
        model.addAttribute("boards", boardService.getAllBoard());
        return "index";
    }

    @GetMapping(value = "/err/denied-page")
    public String accessDenied(Model model){
        model.addAttribute("msg", "권한 없음");
        return "err/deniedPage";
    }

    @GetMapping(value = "/err/expired_user")
    public String expiredUser(Model model){
        model.addAttribute("msg", "로그인 만료");
        return "err/deniedPage";
    }

}
