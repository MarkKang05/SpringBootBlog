package com.board.spring_board.service;

import com.board.spring_board.dto.jwt.TokenDto;
import com.board.spring_board.dto.user.RequestLoginUserDto;
import com.board.spring_board.jwt.TokenProvider;
import com.board.spring_board.model.RefreshToken;
import com.board.spring_board.repository.RefreshTokenRepository;
import com.board.spring_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class AuthServiceImpl {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public TokenDto login(RequestLoginUserDto requestLoginUserDto){
        UsernamePasswordAuthenticationToken authenticationToken =  requestLoginUserDto.toAuthentication();

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findByEmail(requestLoginUserDto.getEmail()).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(120000L));
        refreshToken.setToken(tokenDto.getRefreshToken());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }
}
