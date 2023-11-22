package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.fldsmdfr.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Transactional
    int deleteByUserId(Long userId);

    Optional<RefreshToken> findByToken(String refreshToken);
}
