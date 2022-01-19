package com.board.spring_board.controller;

import com.board.spring_board.dto.jwt.TokenDto;
import com.board.spring_board.dto.user.RequestLoginUserDto;
import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.request.RequestLogin;
import com.board.spring_board.utils.CustomCookie;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        RequestLogin requestLogin = authService.login(loginRequest);

        if (requestLogin.getTokenDto() != null){
            response.addCookie(new CustomCookie("accessToken", requestLogin.getTokenDto().getAccessToken()));
            response.addCookie(new CustomCookie("refreshToken", requestLogin.getTokenDto().getRefreshToken()));
            return ResponseEntity.ok(requestLogin.getTokenDto());
        } else{
            return new ResponseEntity<>(requestLogin.getError(), HttpStatus.BAD_REQUEST);
        }


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

    @PostMapping("/existEmailProc")
    public boolean existEmail(@RequestBody String email){
        return userRepository.existsByEmail(email);
    }

}
