package com.backend.housing.infrastructure.persistence.adapters.chat;

import com.backend.housing.domain.entity.chat.Chat;
import com.backend.housing.domain.ports.out.chat.ChatRepositoryPort;
import com.backend.housing.infrastructure.persistence.entities.chat.ChatEntity;
import com.backend.housing.infrastructure.persistence.entities.users.UserEntity;
import com.backend.housing.infrastructure.persistence.mappers.chat.ChatMapper;
import com.backend.housing.infrastructure.persistence.repositories.chat.ChatJpaRepository;
import com.backend.housing.infrastructure.persistence.repositories.users.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChatRepositoryAdapter implements ChatRepositoryPort {

    private final ChatJpaRepository chatJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public ChatRepositoryAdapter(ChatJpaRepository chatJpaRepository,
                                  UserJpaRepository userJpaRepository) {
        this.chatJpaRepository = chatJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Chat save(Chat chat) {
        ChatEntity entity = ChatMapper.toEntity(chat);
        entity.setTenant(userJpaRepository.findById(chat.getTenantId()).orElseThrow());
        entity.setOwner(userJpaRepository.findById(chat.getOwnerId()).orElseThrow());
        ChatEntity saved = chatJpaRepository.save(entity);
        return ChatMapper.toDomain(saved);
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return chatJpaRepository.findById(id).map(ChatMapper::toDomain);
    }

    @Override
    public Optional<Chat> findByTenantAndOwnerAndProperty(Long tenantId, Long ownerId, Long propertyId) {
        return chatJpaRepository.findByTenantAndOwnerAndProperty(tenantId, ownerId, propertyId)
                .map(ChatMapper::toDomain);
    }

    @Override
    public List<Chat> findByUserId(Long userId) {
        return chatJpaRepository.findByUserId(userId).stream()
                .map(ChatMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByIdAndUserId(Long chatId, Long userId) {
        return chatJpaRepository.existsByIdAndUserId(chatId, userId);
    }
}