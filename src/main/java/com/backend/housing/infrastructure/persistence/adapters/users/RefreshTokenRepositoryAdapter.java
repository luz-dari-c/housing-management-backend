package com.backend.housing.infrastructure.persistence.adapters.users;

import com.backend.housing.domain.entity.users.RefreshToken;
import com.backend.housing.domain.ports.out.users.RefreshTokenRepositoryPort;
import com.backend.housing.infrastructure.persistence.entities.users.*;
import com.backend.housing.infrastructure.persistence.repositories.users.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository jpaRepository;

    public RefreshTokenRepositoryAdapter(RefreshTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = toEntity(refreshToken);
        RefreshTokenEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(this::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(Long userId) {
        jpaRepository.revokeAllByUserId(userId);
    }

    @Override
    @Transactional
    public void revokeByToken(String token) {
        jpaRepository.revokeByToken(token);
    }

    private RefreshTokenEntity toEntity(RefreshToken domain) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setToken(domain.getToken());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setRevoked(domain.isRevoked());
        return entity;
    }

    private RefreshToken toDomain(RefreshTokenEntity entity) {
        RefreshToken domain = new RefreshToken();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setToken(entity.getToken());
        domain.setExpiresAt(entity.getExpiresAt());
        domain.setRevoked(entity.isRevoked());
        return domain;
    }
}