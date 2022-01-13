package com.board.spring_board.payload.response;

import com.board.spring_board.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter @Setter
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private Role role;

    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

}
