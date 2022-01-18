package com.board.spring_board.controller;

import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.UserService;
import com.board.spring_board.utils.HttpSessionUtils;
import com.board.spring_board.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class UserController {


    // Sign Up Page
    @GetMapping("/signupPage")
    public String signupPage(){
        return "/user/userCreate";
    }

    // Log In Page
    @GetMapping("/loginPage")
    public String loginPage(@RequestParam String error, Model model){
        if (error!="")
            model.addAttribute("error", error);

        return "/user/userLogin";
    }



}
