package com.backend.housing.domain.ports.in.chat;

import java.util.List;

import com.backend.housing.domain.entity.chat.Chat;

public interface ChatUseCase {
    Chat startChat(Long tenantId, Long ownerId, Long propertyId);
    List<Chat> getUserChats(Long userId);
    Chat getChatDetail(Long chatId, Long userId);
    void archiveChat(Long chatId, Long userId);
}