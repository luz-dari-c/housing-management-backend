package com.backend.housing.application.dto.response.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class NotificationResponse {
    private UUID id;
    private String type;
    private String title;
    private String message;
    private boolean read;
    private UUID contractId;
    private LocalDateTime createdAt;
}