package com.board.spring_board.jwt;

import com.board.spring_board.dto.jwt.TokenDto;
import com.board.spring_board.model.RefreshToken;
import com.board.spring_board.utils.CustomCookie;
import com.board.spring_board.repository.RefreshTokenRepository;
import com.board.spring_board.service.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal");
        TokenDto tokenDto = parseToken(request);
        String accessToken;
        String refreshToken;
        try {
            accessToken = tokenDto.getAccessToken();
            refreshToken = tokenDto.getRefreshToken();
        } catch (Exception e) {
            accessToken = null;
            refreshToken = null;
        }

        if(accessToken != null && tokenProvider.validateJwtToken(accessToken)){
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if(accessToken != null && tokenProvider.validateJwtToken(refreshToken)){ // accesstoken만료/ 재발급
            Authentication authentication = tokenProvider.getAuthentication(accessToken);

            RefreshToken savedRefreshToken = refreshTokenService.getRefreshTokenByUserEmail(authentication.getName());
            if (!refreshToken.equals(savedRefreshToken.getToken()))
                throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");

            TokenDto issuedTokenDto = tokenProvider.generateTokenDto(authentication);

            RefreshToken newRefreshToken = savedRefreshToken.updateToken(issuedTokenDto.getRefreshToken());
            refreshTokenRepository.save(newRefreshToken);
            response.addCookie(new CustomCookie("accessToken", issuedTokenDto.getAccessToken()));
            response.addCookie(new CustomCookie("refreshToken", issuedTokenDto.getRefreshToken()));
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(issuedTokenDto.getAccessToken()));
        } else if (accessToken!=null){
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            refreshTokenService.deleteByUserEmail(authentication.getName());
            response.addCookie(new CustomCookie("accessToken", null, 0));
            response.addCookie(new CustomCookie("refreshToken", null, 0));
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private TokenDto parseToken(HttpServletRequest request) {
        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");
        String accessToken;
        String refreshToken;
        try {
            accessToken = accessTokenCookie.getValue();
            refreshToken= refreshTokenCookie.getValue();
            if (StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken))
                return TokenDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
        } catch (NullPointerException e){
            return null;
        }
        return null;
    }
}
