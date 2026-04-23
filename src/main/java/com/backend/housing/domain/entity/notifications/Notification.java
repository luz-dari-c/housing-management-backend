package com.backend.housing.domain.entity.notifications;

import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.notifications.valueobjects.NotificationId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

import java.time.LocalDateTime;
import java.util.Objects;

public class Notification {

    private final NotificationId id;
    private final Long userId;
    private final NotificationType type;
    private final String title;
    private final String message;
    private boolean read;
    private final ContractId contractId;
    private final LocalDateTime createdAt;

    private Notification(NotificationId id,
                         Long userId,
                         NotificationType type,
                         String title,
                         String message,
                         boolean read,
                         ContractId contractId,
                         LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "NotificationId cannot be null");
        this.userId = Objects.requireNonNull(userId, "UserId cannot be null");
        this.type = Objects.requireNonNull(type, "NotificationType cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.message = Objects.requireNonNull(message, "Message cannot be null");
        this.read = read;
        this.contractId = contractId;
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");

        validateTitle();
        validateMessage();
    }

    public static Notification create(Long userId,
                                      NotificationType type,
                                      String title,
                                      String message,
                                      ContractId contractId) {
        return new Notification(
                NotificationId.generate(),
                userId,
                type,
                title,
                message,
                false,
                contractId,
                LocalDateTime.now()
        );
    }

    public static Notification reconstitute(NotificationId id,
                                            Long userId,
                                            NotificationType type,
                                            String title,
                                            String message,
                                            boolean read,
                                            ContractId contractId,
                                            LocalDateTime createdAt) {
        return new Notification(
                id,
                userId,
                type,
                title,
                message,
                read,
                contractId,
                createdAt
        );
    }

    public void markAsRead() {
        if (this.read) {
            return;
        }
        this.read = true;
    }

    private void validateTitle() {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Notification title cannot be blank");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Notification title cannot exceed 255 characters");
        }
    }

    private void validateMessage() {
        if (message.isBlank()) {
            throw new IllegalArgumentException("Notification message cannot be blank");
        }
    }

    public NotificationId getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public ContractId getContractId() {
        return contractId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}