package com.backend.housing.domain.ports.out.chat;

import java.util.List;
import java.util.Optional;

import com.backend.housing.domain.entity.chat.Message;

public interface MessageRepositoryPort {
    Message save(Message message);
    List<Message> findByChatId(Long chatId);
    List<Message> findUnreadByChatIdAndUserId(Long chatId, Long userId);
    void markAsSeen(Long chatId, Long userId);
    Long countUnreadByUserId(Long userId);
    Optional<Message> findLastByChatId(Long chatId);
}