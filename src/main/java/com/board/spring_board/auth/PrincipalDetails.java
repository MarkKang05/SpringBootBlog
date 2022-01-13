package com.board.spring_board.auth;

import com.board.spring_board.model.Role;
import com.board.spring_board.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PrincipalDetails implements UserDetails {
    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(Long id, String username, String email, String password, Role role) {
        this.user = new User(id, username, email, password, role);
    }

    public User getUser() {
        return user;
    }

    public static PrincipalDetails build(User user) {

        return new PrincipalDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_"+String.valueOf(user.getRole()));

        roles.forEach(r -> {
            authorities.add(() -> { return r; });
        });

//        authorities.forEach(r -> {
//            System.out.println(r.getAuthority());
//        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
