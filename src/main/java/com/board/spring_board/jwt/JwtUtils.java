package com.board.spring_board.jwt;

import com.board.spring_board.auth.PrincipalDetails;
import com.board.spring_board.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    public Long getUserIdFromJwtToken(String token){
        String username = Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token).getBody().getSubject();
        System.out.println("username" + username);
        return userService.getIdByUsername(username);
    }

    public String generateJwtToken(PrincipalDetails userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JwtProperties.EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token).getBody().getSubject();
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

}
