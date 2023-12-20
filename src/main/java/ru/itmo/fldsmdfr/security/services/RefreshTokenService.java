package ru.itmo.fldsmdfr.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.RefreshToken;
import ru.itmo.fldsmdfr.repositories.RefreshTokenRepository;
import ru.itmo.fldsmdfr.repositories.UserRepository;
import ru.itmo.fldsmdfr.security.exceptions.TokenRefreshException;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${FLDSMDFR.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationsMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken).get();
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationsMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public boolean isVerifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return true;
    }

    public void deleteRefreshToken(Long id) {
        refreshTokenRepository.deleteByUserId(id);
    }
}

