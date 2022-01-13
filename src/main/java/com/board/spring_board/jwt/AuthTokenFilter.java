package com.board.spring_board.jwt;

import com.board.spring_board.model.RefreshToken;
import com.board.spring_board.model.User;
import com.board.spring_board.payload.request.CookieBuilder;
import com.board.spring_board.payload.response.TokenRefreshResponse;
import com.board.spring_board.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class AuthTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal");
        try {
            // TODO: 2022/01/13 코드 간소화
            Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
            Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");
            String accessToken = accessTokenCookie.getValue();
            String refreshToken = refreshTokenCookie.getValue();

            if (accessToken != null && jwtUtils.validateJwtToken(accessToken)) {
                String username = jwtUtils.getUserNameFromJwtToken(accessToken);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if(accessToken != null ){
                //refresh 쿠키를 이용해서 valid확인하고 유효하면 쿠키에 새로운 엑세스토큰 발급

                try { // 액세스 토큰을 위한 리프레시 토큰이 유효함
                    logger.info("refresh accessToken process");
                    RefreshToken requestRefreshToken = refreshTokenService.findByToken(refreshToken).get();
                    User refreshUser = refreshTokenService.verifyExpiration(requestRefreshToken).getUser();
                    String token = jwtUtils.generateTokenFromUsername(refreshUser.getEmail()); // 새로운 Access token
                    response.addCookie(new CookieBuilder("accessToken", token));

                    String username = jwtUtils.getUserNameFromJwtToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // 리프레시 토큰이 만료됨 (로그인 페이지로 보내야함.) or 리프레시 토큰을 찾을수 없음
                // 프론트엔드에서 403을 받았을 때 에러메세지 확인 후
                // 권한 오류이면 권한 없음 페이지 출력 -> 이 경우는 스프링 시큐리티 자체 필터를 이용함
                // 리프레시 토큰이 만료된경우 로그인 페이지로 이동
                catch (NoSuchElementException e) { // 리프레시 토큰 만료됨 -> 로그인 화면
                    logger.info("refresh token 만료");
                    response.addCookie(new CookieBuilder("accessToken", ""));
                    response.addCookie(new CookieBuilder("refreshToken", ""));
                }

            }
        } catch (Exception e) { //쿠키 존재 x -> 로그인 화면
            logger.info(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // TODO: 2022/01/13 cookie를 이용한 방식으로 바꾸기
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}
