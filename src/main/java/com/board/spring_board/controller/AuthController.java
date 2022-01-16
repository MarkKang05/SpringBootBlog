package com.board.spring_board.controller;

import com.board.spring_board.auth.PrincipalDetails;
import com.board.spring_board.dto.jwt.TokenDto;
import com.board.spring_board.dto.user.RequestLoginUserDto;
import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.exception.TokenRefreshException;
import com.board.spring_board.jwt.JwtUtils;
import com.board.spring_board.model.RefreshToken;
import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.payload.request.CookieBuilder;
import com.board.spring_board.payload.request.LogOutRequest;
import com.board.spring_board.payload.request.TokenRefreshRequest;
import com.board.spring_board.payload.response.JwtResponse;
import com.board.spring_board.payload.response.MessageResponse;
import com.board.spring_board.payload.response.TokenRefreshResponse;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.AuthServiceImpl;
import com.board.spring_board.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    private final AuthServiceImpl authService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody RequestLoginUserDto loginRequest, HttpServletResponse response) {
        //로그인 프로세스 시작
        System.out.println("login Process start");

        TokenDto tokenDto = authService.login(loginRequest);

//        cookie
        response.addCookie(new CookieBuilder("accessToken", tokenDto.getAccessToken()));
        response.addCookie(new CookieBuilder("refreshToken", tokenDto.getRefreshToken()));

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/signup")
    public Long registerUser(@RequestBody RequestSaveUserDto request) {

        // Create new user's account
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user).getId();

//        return ResponseEntity.ok();
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request, HttpServletResponse response) { // 새로고침할 refresh토큰
        String requestRefreshToken = request.getRefreshToken();
        System.out.println(requestRefreshToken);

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken).get();
        try {
            User refreshUser = refreshTokenService.verifyExpiration(refreshToken).getUser();
            System.out.println(refreshUser.toString());
            String token = jwtUtils.generateTokenFromUsername(refreshUser.getEmail()); // 새로운 Access token
            TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(requestRefreshToken, token);
            System.out.println(tokenRefreshResponse.getAccessToken());


            Cookie accessTokenCookie = new Cookie("accessToken", token);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken.getToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new TokenRefreshResponse(requestRefreshToken, token));
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;

    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser(@RequestBody LogOutRequest logOutRequest) {
//        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
//        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
//    }

    @GetMapping("/user/test")
    public String test(){
        return "userTest";
    }


}
