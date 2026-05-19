package com.backend.housing.domain.ports.in.chat;

import java.util.List;

import com.backend.housing.domain.entity.chat.Message;

public interface MessageUseCase {
    Message sendMessage(Long chatId, Long senderId, String content);
    List<Message> getChatMessages(Long chatId, Long userId);
    void markMessagesAsSeen(Long chatId, Long userId);
    Long getUnreadCount(Long userId);
}