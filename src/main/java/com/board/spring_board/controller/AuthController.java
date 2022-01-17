package com.board.spring_board.controller;

import com.board.spring_board.dto.jwt.TokenDto;
import com.board.spring_board.dto.user.RequestLoginUserDto;
import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.RefreshToken;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.payload.request.CookieBuilder;
import com.board.spring_board.payload.request.TokenRefreshRequest;
import com.board.spring_board.payload.response.TokenRefreshResponse;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.AuthServiceImpl;
import com.board.spring_board.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
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
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user).getId();

//        return ResponseEntity.ok();
    }


    @GetMapping("/user/test")
    public String test(){
        return "userTest";
    }


}
