package com.board.spring_board.jwt;

import com.board.spring_board.dto.jwt.TokenDto;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String BEARER_TYPE = "bearer";

    public TokenDto generateTokenDto(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now+JwtProperties.EXPIRATION_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now+120000L))
                .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                .compact();
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null)
            throw new RuntimeException("권한 정보가 없는 토큰");
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        System.out.println(authorities.toString());
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        for(String role : claims.get("auth").toString().split(",")){
//            String role2 = "Role_"+role;
//            authorities.add(new SimpleGrantedAuthority(role2));
//        }
//        return authorities;
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//        return new UsernamePasswordAuthenticationToken(user, "");
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.info("Invalid JWT signature: " +  e.getMessage());
        } catch (MalformedJwtException e) {
            logger.info("Invalid JWT token: " +  e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.info("JWT token is expired: " +  e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.info("JWT token is unsupported: " +  e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.info("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }


}
