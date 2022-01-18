package com.board.spring_board.utils;

import com.board.spring_board.config.CorsConfig;
import com.board.spring_board.controller.CustomAuthenticationProvider;
import com.board.spring_board.handler.CustomAccessDeniedHandler;
import com.board.spring_board.handler.CustomAuthenticationEntryPoint;
import com.board.spring_board.handler.CustomLogoutHandler;
import com.board.spring_board.jwt.*;
import com.board.spring_board.repository.UserRepository;
import com.board.spring_board.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
//        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        return new CustomAuthenticationProvider();
//    }

    @Bean
    public CustomLogoutHandler customLogoutHandlerBean(){
        return new CustomLogoutHandler();
    }
//    @Bean
//    public CustomAuthFailureHandler customAuthFailureHandlerBean() {return new CustomAuthFailureHandler();}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .addFilter(corsConfig.corsFilter())
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
//                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/auth/**").hasRole("ANONYMOUS")
                .antMatchers("/board/details/**").permitAll()
                .antMatchers("/board/**").hasRole("USER")
                .antMatchers("/resources/**").permitAll()
                .anyRequest()
                .permitAll()
            .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(customLogoutHandlerBean())
                .logoutSuccessUrl("/");

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());

    }
}
