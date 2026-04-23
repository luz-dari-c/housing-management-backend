package com.backend.housing.infrastructure.persistence.mappers.users;

import com.backend.housing.domain.entity.users.PasswordResetToken;
import com.backend.housing.infrastructure.persistence.entities.users.PasswordResetTokenEntity;

public class PasswordResetMapper {

    public static PasswordResetTokenEntity toEntity(PasswordResetToken token) {
        if (token == null) return null;
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setId(token.getId());
        entity.setEmail(token.getEmail());
        entity.setCode(token.getCode());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setUsed(token.isUsed());
        return entity;
    }

    public static PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        if (entity == null) return null;
        PasswordResetToken token = new PasswordResetToken();
        token.setId(entity.getId());
        token.setEmail(entity.getEmail());
        token.setCode(entity.getCode());
        token.setExpiresAt(entity.getExpiresAt());
        token.setUsed(entity.isUsed());
        return token;
    }
}