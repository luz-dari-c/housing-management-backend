package com.backend.housing.domain.ports.out.users;

import com.backend.housing.domain.entity.users.PasswordResetToken;  
import java.util.Optional;

public interface PasswordResetRepositoryPort {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByEmailAndCode(String email, String code);
    Optional<PasswordResetToken> findValidToken(String email, String code);
    void markAsUsed(String email, String code);
    void invalidatePreviousTokens(String email);
}