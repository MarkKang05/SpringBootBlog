package com.board.spring_board.service;

import com.board.spring_board.dto.user.RequestSaveUserDto;
import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(RequestSaveUserDto requestSaveUserDto) {
//        System.out.println(requestSaveUserDto.getPassword());
        String encodedPassword = passwordEncoder.encode(requestSaveUserDto.getPassword());
        requestSaveUserDto.setPassword(encodedPassword);
        requestSaveUserDto.setRole(Role.ROLE_USER);
        userRepository.save(requestSaveUserDto.toEntity());
        System.out.println("Sign up!!");
    }

    @Override
    public Long getIdByUsername(String username) {
        if (userRepository.findByEmail(username).isPresent()){
            return userRepository.findByEmail(username).get().getId();
        }
        return null;
    }

    public User getCurrentUser(){
        return userRepository.findById(SecurityUtils.getCurrentUserId()).get();
    }
}
