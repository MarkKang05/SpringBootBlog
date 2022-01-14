package com.board.spring_board.handler;

import com.board.spring_board.jwt.JwtUtils;
import com.board.spring_board.payload.request.CookieBuilder;
import com.board.spring_board.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        String accessToken = accessTokenCookie.getValue();
        Long userId = jwtUtils.getUserIdFromJwtToken(accessToken);
        refreshTokenService.deleteByUserId(userId);
        response.addCookie(new CookieBuilder("accessToken", ""));
        response.addCookie(new CookieBuilder("refreshToken", ""));
        SecurityContextHolder.clearContext();
    }
}
