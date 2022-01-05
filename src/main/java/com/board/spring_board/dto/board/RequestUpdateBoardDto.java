package com.board.spring_board.dto.board;

import com.board.spring_board.model.Board;
import com.board.spring_board.utils.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RequestUpdateBoardDto extends BaseTimeEntity {

    private String title;
    private String description;

    @Builder
    public RequestUpdateBoardDto(String title, String description) {
        this.title = title;
        this.description = description;
    }


    public Board toEntity(){
        return Board.builder()
                .title(title)
                .description(description)
                .build();
    }
}
