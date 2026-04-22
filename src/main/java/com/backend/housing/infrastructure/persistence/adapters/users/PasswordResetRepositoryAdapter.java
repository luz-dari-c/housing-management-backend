package com.backend.housing.infrastructure.persistence.adapters.users;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.domain.entity.users.PasswordResetToken;
import com.backend.housing.domain.ports.out.users.PasswordResetRepositoryPort;
import com.backend.housing.infrastructure.persistence.entities.users.PasswordResetTokenEntity;
import com.backend.housing.infrastructure.persistence.mappers.users.PasswordResetMapper;
import com.backend.housing.infrastructure.persistence.repositories.users.PasswordResetJpaRepository;

import java.util.Optional;

@Component
public class PasswordResetRepositoryAdapter implements PasswordResetRepositoryPort {

    private final PasswordResetJpaRepository jpaRepository;

    public PasswordResetRepositoryAdapter(PasswordResetJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = PasswordResetMapper.toEntity(token);
        PasswordResetTokenEntity saved = jpaRepository.save(entity);
        return PasswordResetMapper.toDomain(saved);
    }

    @Override
    public Optional<PasswordResetToken> findByEmailAndCode(String email, String code) {
        return jpaRepository.findByEmailAndCode(email, code)
                .map(PasswordResetMapper::toDomain);
    }

    @Override
    public Optional<PasswordResetToken> findValidToken(String email, String code) {
        return jpaRepository.findValidToken(email, code)
                .map(PasswordResetMapper::toDomain);
    }

    @Override
    @Transactional
    public void markAsUsed(String email, String code) {
        jpaRepository.markAsUsed(email, code);
    }

    @Override
    @Transactional
    public void invalidatePreviousTokens(String email) {
        jpaRepository.invalidatePreviousTokens(email);
    }

}