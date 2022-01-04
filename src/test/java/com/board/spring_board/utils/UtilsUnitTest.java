package com.board.spring_board.utils;

import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.UserServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UtilsUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeAll
    public static void beforeClass() {
        System.out.println("-----테스트 시작-----");
    }

    @AfterAll
    public static void afterClass() {
        System.out.println("-----테스트 종료-----");
    }

//    @DisplayName("BaseTimeEntityTest")
    @Test
    @Transactional
    @Rollback(value = true)
    public void BaseTimeEntityTest() {

        //given
        LocalDateTime now = LocalDateTime.of(2022,1,3,0,0,0);

        userRepository.save(User.builder()
                .username("TestUsername")
                .email("TestUserEmail")
                .password("password")
                .role(Role.USER)
                .build());


        //when
        List<User> userLists = userRepository.findAll();
        User user = userLists.get(0);

        System.out.println("createdDate= "+user.getCreatedDate()+"\tmodifiedDate= "+user.getModifiedData());

        //then
        assertTrue(user.getCreatedDate().isAfter(now));
    }

//    @DisplayName("BaseTimeEntityTest2")
    @Test
    @Transactional
    @Rollback(value = true)
    public void BaseTimeEntityTest2() {

        //given
        LocalDateTime now = LocalDateTime.of(2022,1,3,0,0,0);

        userRepository.save(User.builder()
                .username("TestUsername")
                .email("TestUserEmail")
                .password("password")
                .role(Role.USER)
                .build());


        //when
        List<User> userLists = userRepository.findAll();
        User user = userLists.get(0);

        System.out.println("createdDate= "+user.getCreatedDate()+"\tmodifiedDate= "+user.getModifiedData());

        //then
        assertTrue(user.getCreatedDate().isAfter(now));
    }
}
