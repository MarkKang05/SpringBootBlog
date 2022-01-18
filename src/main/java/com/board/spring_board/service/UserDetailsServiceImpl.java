package com.board.spring_board.service;

import com.board.spring_board.model.User;
import com.board.spring_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("UserDetailsService 진입");
        return userRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new InternalAuthenticationServiceException("사용자를 찾을 수 없음"));
//        logger.info("Email: "+username);
//        if(userRepository.findByEmail(username).isPresent()){
//            logger.info("user present");
//            return createUserDetails(userRepository.findByEmail(username).get());
//        } else{
//            logger.info("user not present");
//            throw new InternalAuthenticationServiceException("errrrrrrrrooor");
//        }
    }

    private UserDetails createUserDetails(User user){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().toString());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );

    }
}
