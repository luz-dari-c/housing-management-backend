package com.backend.housing.domain.ports.out.chat;

import java.util.List;
import java.util.Optional;

import com.backend.housing.domain.entity.chat.Chat;

public interface ChatRepositoryPort {
    Chat save(Chat chat);
    Optional<Chat> findById(Long id);
    Optional<Chat> findByTenantAndOwnerAndProperty(Long tenantId, Long ownerId, Long propertyId);
    List<Chat> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long chatId, Long userId);
}