package com.backend.housing.application.services.chat;

import com.backend.housing.domain.entity.chat.Chat;
import com.backend.housing.domain.ports.in.chat.ChatUseCase;
import com.backend.housing.domain.ports.out.chat.ChatRepositoryPort;
import com.backend.housing.domain.ports.out.chat.MessageRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatService implements ChatUseCase {

    private final ChatRepositoryPort chatRepositoryPort;
    private final MessageRepositoryPort messageRepositoryPort;

    public ChatService(ChatRepositoryPort chatRepositoryPort,
                       MessageRepositoryPort messageRepositoryPort) {
        this.chatRepositoryPort = chatRepositoryPort;
        this.messageRepositoryPort = messageRepositoryPort;
    }

    @Override
    public Chat startChat(Long tenantId, Long ownerId, Long propertyId) {
        if (tenantId.equals(ownerId)) {
            throw new RuntimeException("No puedes iniciar un chat contigo mismo");
        }

        return chatRepositoryPort.findByTenantAndOwnerAndProperty(tenantId, ownerId, propertyId)
                .orElseGet(() -> {
                    Chat chat = new Chat();
                    chat.setTenantId(tenantId);
                    chat.setOwnerId(ownerId);
                    chat.setPropertyId(propertyId);
                    return chatRepositoryPort.save(chat);
                });
    }

    @Override
    public List<Chat> getUserChats(Long userId) {
        List<Chat> chats = chatRepositoryPort.findByUserId(userId);
        for (Chat chat : chats) {
            Optional<com.backend.housing.domain.entity.chat.Message> lastMsg = messageRepositoryPort.findLastByChatId(chat.getId());
            if (lastMsg.isPresent()) {
                chat.setLastMessage(lastMsg.get());
            }
        }
        return chats;
    }

    @Override
    public Chat getChatDetail(Long chatId, Long userId) {
        if (!chatRepositoryPort.existsByIdAndUserId(chatId, userId)) {
            throw new RuntimeException("Chat no encontrado o no tienes acceso");
        }
        return chatRepositoryPort.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
    }

    @Override
    public void archiveChat(Long chatId, Long userId) {
        Chat chat = chatRepositoryPort.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        if (!chat.getTenantId().equals(userId) && !chat.getOwnerId().equals(userId)) {
            throw new RuntimeException("No tienes acceso a este chat");
        }
        chat.setArchived(true);
        chatRepositoryPort.save(chat);
    }
}