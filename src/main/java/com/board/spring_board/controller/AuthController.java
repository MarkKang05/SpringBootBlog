package com.board.spring_board.controller;

import com.board.spring_board.dto.jwt.TokenDto;
import com.board.spring_board.dto.user.RequestLoginUserDto;
import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.utils.CustomCookie;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody RequestLoginUserDto loginRequest, HttpServletResponse response) {
        System.out.println("login");
        TokenDto tokenDto = authService.login(loginRequest);

        response.addCookie(new CustomCookie("accessToken", tokenDto.getAccessToken()));
        response.addCookie(new CustomCookie("refreshToken", tokenDto.getRefreshToken()));

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/signup")
    public Long registerUser(@RequestBody RequestSaveUserDto request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user).getId();

    }

}
