package com.board.spring_board.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentUserId(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName()==null){
            throw new RuntimeException("None Authentication In SecurityContext");
        }
        return Long.parseLong(authentication.getName());
    }
}
