package com.board.spring_board.dto.user;

import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RequestSaveUserDto {
    private String email;
    private String username;
    private String password;
    private Role role;

//    @Builder
    public RequestSaveUserDto(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
