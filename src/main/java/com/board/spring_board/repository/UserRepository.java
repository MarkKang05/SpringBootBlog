package com.board.spring_board.repository;

import com.board.spring_board.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//에러나면 @Repository 추가 해보기

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
