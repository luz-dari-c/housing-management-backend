package com.backend.housing.infrastructure.persistence.adapters.chat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.domain.entity.chat.Message;
import com.backend.housing.domain.ports.out.chat.MessageRepositoryPort;
import com.backend.housing.infrastructure.persistence.mappers.chat.ChatMapper;
import com.backend.housing.infrastructure.persistence.repositories.chat.ChatJpaRepository;
import com.backend.housing.infrastructure.persistence.repositories.chat.MessageJpaRepository;
import com.backend.housing.infrastructure.persistence.repositories.users.UserJpaRepository;

@Component
public class MessageRepositoryAdapter implements MessageRepositoryPort {

    private final MessageJpaRepository messageJpaRepository;
    private final ChatJpaRepository chatJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public MessageRepositoryAdapter(MessageJpaRepository messageJpaRepository,
                                     ChatJpaRepository chatJpaRepository,
                                     UserJpaRepository userJpaRepository) {
        this.messageJpaRepository = messageJpaRepository;
        this.chatJpaRepository = chatJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Message save(Message message) {
        var entity = ChatMapper.toEntity(message);
        entity.setChat(chatJpaRepository.findById(message.getChatId()).orElseThrow());
        entity.setSender(userJpaRepository.findById(message.getSenderId()).orElseThrow());
        var saved = messageJpaRepository.save(entity);
        return ChatMapper.toDomain(saved);
    }

    @Override
    public List<Message> findByChatId(Long chatId) {
        return messageJpaRepository.findByChatIdOrderBySentAtAsc(chatId).stream()
                .map(ChatMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findUnreadByChatIdAndUserId(Long chatId, Long userId) {
        return messageJpaRepository.findUnreadByChatIdAndUserId(chatId, userId).stream()
                .map(ChatMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsSeen(Long chatId, Long userId) {
        messageJpaRepository.markAsSeen(chatId, userId);
    }

    @Override
    public Long countUnreadByUserId(Long userId) {
        return messageJpaRepository.countUnreadByUserId(userId);
    }

    @Override
    public Optional<Message> findLastByChatId(Long chatId) {
        return messageJpaRepository.findFirstByChatIdOrderBySentAtDesc(chatId)
                .map(ChatMapper::toDomain);
    }
}