package com.board.spring_board.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){ // 로그인 하지 않은 사용자
            request.setAttribute("msg", "로그인 하지 않았습니다.");
            request.setAttribute("nextPage", "/login");
            SecurityContextHolder.clearContext();
        } else{

        }
        request.getRequestDispatcher("/err/expired_user").forward(request, response);

    }
}
