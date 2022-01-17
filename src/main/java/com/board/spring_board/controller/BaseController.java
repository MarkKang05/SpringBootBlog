package com.board.spring_board.controller;

import com.board.spring_board.model.User;
import com.board.spring_board.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class BaseController {

    @Autowired
    private SecurityUtils securityUtils;

    @ModelAttribute
    public void modelUsername(Model model) {
        try {
            model.addAttribute("user", securityUtils.getUserFromSecurity());
        }catch (Exception e){
        }
    }
}
