package com.backend.housing.infrastructure.persistence.entities.notifications;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "read", nullable = false)
    private boolean read;

    @Column(name = "contract_id")
    private UUID contractId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public NotificationEntity(Long userId,
                              String type,
                              String title,
                              String message,
                              boolean read,
                              UUID contractId,
                              LocalDateTime createdAt) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.read = read;
        this.contractId = contractId;
        this.createdAt = createdAt;
    }
}