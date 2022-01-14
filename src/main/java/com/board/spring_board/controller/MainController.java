package com.board.spring_board.controller;

import com.board.spring_board.auth.PrincipalDetails;
import com.board.spring_board.jwt.JwtUtils;
import com.board.spring_board.model.User;
import com.board.spring_board.service.BoardService;
import com.board.spring_board.utils.HttpSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

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

        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");

        if(accessTokenCookie != null) {
            String accessToken = accessTokenCookie.getValue();
            if(!accessToken.equals("")) {
                model.addAttribute("username", jwtUtils.getUserNameFromJwtToken(accessToken));
            }
        }


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
