package com.board.spring_board.controller;

import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.UserService;
import com.board.spring_board.service.UserServiceImpl;
import com.board.spring_board.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
//    private UserServiceImpl userService;
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Sign Up
    @GetMapping("/signup")
    public String signupPage(HttpSession session){
        if(HttpSessionUtils.isLoginUser(session)){
            return "redirect:/";
        }
        return "user/userCreate";
    }

    @PostMapping("/user/signupProc")
    public String signupProc(RequestSaveUserDto requestSaveUserDto){
        userService.createUser(requestSaveUserDto);
        return "redirect:/login";
    }

    // Log In
    @GetMapping("/login")
    public String loginPage(HttpSession session){
        if(HttpSessionUtils.isLoginUser(session))
            return "redirect:/";

        return "/user/userLogin";
    }

    @PostMapping("/user/loginProc")
    public String loginProc(RequestSaveUserDto requestSaveUserDto, HttpSession session){
        User loginUser = requestSaveUserDto.toEntity();
        if(userRepository.findByEmail(loginUser.getEmail()) == null) {
            System.out.println("Not found email");
            return "redirect:/";
        } else{
//            System.out.println(userRepository.findByEmail(loginUser.getEmail()).getPassword());
//            System.out.println(passwordEncoder.encode("qwer2"));
            if(passwordEncoder.matches(loginUser.getPassword(), userRepository.findByEmail(loginUser.getEmail()).getPassword() ) ) {
                loginUser = userRepository.findByEmail(requestSaveUserDto.getEmail());
                session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);
                return "redirect:/";
            }
            else{
                System.out.println("Not match password");
                return "redirect:/";
            }
        }

    }







}
