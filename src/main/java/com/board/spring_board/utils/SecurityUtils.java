package com.board.spring_board.utils;

import com.board.spring_board.model.User;
import com.board.spring_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    @Autowired
    private UserRepository userRepository;

    public User getUserFromSecurity(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName()==null){
            throw new RuntimeException("None Authentication In SecurityContext");
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElse(null);
        return user;
    }

    public boolean isLogin(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName()==null){
            return false;
        }
        return true;
    }
}
