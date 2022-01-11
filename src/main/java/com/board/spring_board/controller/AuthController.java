package com.board.spring_board.controller;

import com.board.spring_board.auth.PrincipalDetails;
import com.board.spring_board.dto.user.RequestLoginUserDto;
import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.exception.TokenRefreshException;
import com.board.spring_board.jwt.JwtUtils;
import com.board.spring_board.model.RefreshToken;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.payload.request.LogOutRequest;
import com.board.spring_board.payload.request.TokenRefreshRequest;
import com.board.spring_board.payload.response.JwtResponse;
import com.board.spring_board.payload.response.MessageResponse;
import com.board.spring_board.payload.response.TokenRefreshResponse;
import com.board.spring_board.repository.UserRepository;
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



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody RequestLoginUserDto loginRequest) {
        //로그인 프로세스 시작
        System.out.println("login Process start");

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        PrincipalDetails userDetailsTemp = (PrincipalDetails) authentication.getPrincipal();
        String tem = userDetailsTemp.getUser().getEmail();
        userDetailsTemp.getUser().setEmail(userDetailsTemp.getUser().getUsername());
        userDetailsTemp.getUser().setUsername(tem);
        PrincipalDetails userDetails = userDetailsTemp;

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUser().getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUser().getId(),
                userDetails.getUser().getUsername(), userDetails.getUser().getEmail(), userDetails.getUser().getRole()));
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
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken).get();
        try {
            System.out.println("hello");
            User refreshUser = refreshTokenService.verifyExpiration(refreshToken).getUser();
            String token = jwtUtils.generateTokenFromUsername(refreshUser.getEmail());
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;

//        return refreshTokenService.findByToken(requestRefreshToken)
////                .map(refreshTokenService::verifyExpiration)
//                .map(x -> {
//                    try {
//                        return refreshTokenService.verifyExpiration(x);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                })
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String token = jwtUtils.generateTokenFromUsername(((User) user).getUsername());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
////                .orElsethrow(() -> System.out.println(
////                        "refresh token is not in database!"));
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    @GetMapping("/user/test")
    public String test(){
        return "userTest";
    }

}
