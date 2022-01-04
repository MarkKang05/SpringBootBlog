package com.board.spring_board.dto.board;

import com.board.spring_board.model.Board;
import com.board.spring_board.model.User;
import lombok.Builder;

@Builder
public class RequestSaveBoardDto {
    private String title;
    private String description;
    private User user;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .description(description)
                .views(0L)
                .user(user)
                .build();

    }

    public void setUser(User user) {
        this.user = user;
    }
}
