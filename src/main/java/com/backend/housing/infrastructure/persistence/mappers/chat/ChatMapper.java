package com.backend.housing.infrastructure.persistence.mappers.chat;

import com.backend.housing.domain.entity.chat.Chat;
import com.backend.housing.domain.entity.chat.Message;
import com.backend.housing.infrastructure.persistence.entities.chat.ChatEntity;
import com.backend.housing.infrastructure.persistence.entities.chat.MessageEntity;
import com.backend.housing.infrastructure.persistence.entities.users.UserEntity;

import java.util.stream.Collectors;

public class ChatMapper {

    public static ChatEntity toEntity(Chat chat) {
        if (chat == null) return null;
        ChatEntity entity = new ChatEntity();
        entity.setId(chat.getId());
        entity.setPropertyId(chat.getPropertyId());
        entity.setCreatedAt(chat.getCreatedAt());
        entity.setUpdatedAt(chat.getUpdatedAt());
        entity.setArchived(chat.isArchived());
        return entity;
    }

    public static Chat toDomain(ChatEntity entity) {
        if (entity == null) return null;
        Chat chat = new Chat();
        chat.setId(entity.getId());
        chat.setTenantId(entity.getTenant().getId());
        chat.setOwnerId(entity.getOwner().getId());
        chat.setPropertyId(entity.getPropertyId());
        chat.setCreatedAt(entity.getCreatedAt());
        chat.setUpdatedAt(entity.getUpdatedAt());
        chat.setArchived(entity.isArchived());
        return chat;
    }

    public static MessageEntity toEntity(Message message) {
        if (message == null) return null;
        MessageEntity entity = new MessageEntity();
        entity.setId(message.getId());
        entity.setContent(message.getContent());
        entity.setStatus(MessageEntity.MessageStatus.valueOf(message.getStatus().name()));
        entity.setSentAt(message.getSentAt());
        entity.setSeenAt(message.getSeenAt());
        return entity;
    }

    public static Message toDomain(MessageEntity entity) {
        if (entity == null) return null;
        Message message = new Message();
        message.setId(entity.getId());
        message.setChatId(entity.getChat().getId());
        message.setSenderId(entity.getSender().getId());
        message.setContent(entity.getContent());
        message.setStatus(Message.MessageStatus.valueOf(entity.getStatus().name()));
        message.setSentAt(entity.getSentAt());
        message.setSeenAt(entity.getSeenAt());
        return message;
    }
}