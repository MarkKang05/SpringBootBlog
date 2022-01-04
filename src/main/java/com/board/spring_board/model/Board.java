package com.board.spring_board.model;

import com.board.spring_board.utils.BaseTimeEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private  Long views;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @Builder
    public Board(String title, String description, Long views, User user) {
        this.title = title;
        this.description = description;
        this.views = views;
        this.user = user;
    }
}
