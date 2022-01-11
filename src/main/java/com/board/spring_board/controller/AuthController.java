package com.board.spring_board.controller;

import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/join")
    public Long join(@RequestBody RequestSaveUserDto requestSaveUserDto){
        return userRepository.save(User.builder()
                .username(requestSaveUserDto.getUsername())
                .email(requestSaveUserDto.getEmail())
                .password(passwordEncoder.encode(requestSaveUserDto.getPassword()))
                .role(Role.USER)
                .build()).getId();
    }

//    @PostMapping("/auth/login")
//    public String login(@RequestBody Map<String, String> user){
//        User loginUser = userRepository.findByEmail(user.get("email"))
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일"));
//        if (!passwordEncoder.matches(user.get("password"), loginUser.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호");
//        }
//
//        List<String> roles = new ArrayList<>();
//        roles.add("USER");
//
//        return tokenProvider.createToken(loginUser.getUsername(), roles);
//
//    }

    @GetMapping("/user/test")
    public String test(){
        return "userTest";
    }

}
