package com.backend.housing.domain.entity.chat;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String content;
    private MessageStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime seenAt;

    public enum MessageStatus {
        SENT,
        DELIVERED,
        SEEN
    }

    public Message() {
        this.sentAt = LocalDateTime.now();
        this.status = MessageStatus.SENT;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public LocalDateTime getSeenAt() { return seenAt; }
    public void setSeenAt(LocalDateTime seenAt) { this.seenAt = seenAt; }
}