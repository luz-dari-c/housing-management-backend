package com.backend.housing.application.services.chat;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.domain.entity.chat.Chat;
import com.backend.housing.domain.entity.chat.Message;
import com.backend.housing.domain.ports.in.chat.MessageUseCase;
import com.backend.housing.domain.ports.out.chat.ChatRepositoryPort;
import com.backend.housing.domain.ports.out.chat.MessageRepositoryPort;

@Service
@Transactional
public class MessageService implements MessageUseCase {

    private final ChatRepositoryPort chatRepositoryPort;
    private final MessageRepositoryPort messageRepositoryPort;

    public MessageService(ChatRepositoryPort chatRepositoryPort,
                          MessageRepositoryPort messageRepositoryPort) {
        this.chatRepositoryPort = chatRepositoryPort;
        this.messageRepositoryPort = messageRepositoryPort;
    }

    @Override
    public Message sendMessage(Long chatId, Long senderId, String content) {
        Chat chat = chatRepositoryPort.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        if (!chat.getTenantId().equals(senderId) && !chat.getOwnerId().equals(senderId)) {
            throw new RuntimeException("No perteneces a este chat");
        }

        if (chat.isArchived()) {
            chat.setArchived(false);
            chatRepositoryPort.save(chat);
        }

        Message message = new Message();
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setStatus(Message.MessageStatus.SENT);

        return messageRepositoryPort.save(message);
    }

    @Override
    public List<Message> getChatMessages(Long chatId, Long userId) {
        if (!chatRepositoryPort.existsByIdAndUserId(chatId, userId)) {
            throw new RuntimeException("No tienes acceso a este chat");
        }
        return messageRepositoryPort.findByChatId(chatId);
    }

    @Override
    public void markMessagesAsSeen(Long chatId, Long userId) {
        messageRepositoryPort.markAsSeen(chatId, userId);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return messageRepositoryPort.countUnreadByUserId(userId);
    }
}