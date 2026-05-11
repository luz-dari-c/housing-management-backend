package com.backend.housing.domain.ports.out.users;

import java.util.Optional;

import com.backend.housing.domain.entity.users.RefreshToken;

public interface RefreshTokenRepositoryPort {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    void revokeAllByUserId(Long userId);

    void revokeByToken(String token);
}