package com.board.spring_board.controller;

import com.board.spring_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model){

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userDetails != null)
            model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("boards", boardService.getAllBoard());
        return "index";
    }

    @GetMapping(value = "/err/denied-page")
    public String accessDenied(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("msg", "권한 없음");

        return "err/deniedPage";
    }

    @GetMapping(value = "/err/expired_user")
    public String expiredUser(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("msg", "로그인 만료");
        return "err/deniedPage";
    }

//    @GetMapping("/logout")
//    public String logout(){
//
//        PrincipalDetails user = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(user.getUser());
//
//        return "redirect:/";
//    }
}
