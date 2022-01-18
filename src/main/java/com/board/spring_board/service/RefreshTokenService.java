package com.board.spring_board.service;

import com.board.spring_board.exception.TokenRefreshException;
import com.board.spring_board.jwt.JwtProperties;
import com.board.spring_board.jwt.TokenProvider;
import com.board.spring_board.model.RefreshToken;
import com.board.spring_board.model.User;
import com.board.spring_board.repository.RefreshTokenRepository;
import com.board.spring_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(JwtProperties.REFRESHTOKEN_EXPIRATION_TIME));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) throws Exception {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
//            throw new Exception("Refresh token was expired, please signin ");
        }

        return token;
    }

    @Transactional
    public int deleteByUserEmail(String email) {
        return refreshTokenRepository.deleteByUser(userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Not found User")));
    }

    public RefreshToken getRefreshTokenByUserEmail(String Email){
        User user = userRepository.findByEmail(Email)
                .orElseThrow(() -> new RuntimeException("Not Found User"));
        return refreshTokenRepository.findByUser(user)
                .orElse(null);
//                .orElseThrow(()-> new RuntimeException("Not found RefreshToken"));
    }
//    public RefreshToken reissueRefreshToken(String Email){
//        User user = userRepository.findByEmail(Email).get();
//        RefreshToken invalidRefreshToken = refreshTokenRepository.findByUserId(user.getId())
//                .orElseThrow(()-> new RuntimeException("Not found RefreshToken"));
//
//    }

}
