package com.backend.housing.domain.entity.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private Long id;
    private Long tenantId;
    private Long ownerId;
    private Long propertyId;
    private List<com.backend.housing.domain.entity.chat.Message> messages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean archived;
    private com.backend.housing.domain.entity.chat.Message lastMessage;

    public Chat() {
        this.messages = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public List<com.backend.housing.domain.entity.chat.Message> getMessages() { return messages; }
    public void setMessages(List<com.backend.housing.domain.entity.chat.Message> messages) { this.messages = messages; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }

    public com.backend.housing.domain.entity.chat.Message getLastMessage() { return lastMessage; }
    public void setLastMessage(com.backend.housing.domain.entity.chat.Message lastMessage) { this.lastMessage = lastMessage; }

    public Long getOtherUserId(Long currentUserId) {
        return tenantId.equals(currentUserId) ? ownerId : tenantId;
    }
}