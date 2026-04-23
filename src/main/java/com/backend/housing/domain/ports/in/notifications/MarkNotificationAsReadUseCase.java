package com.backend.housing.domain.ports.in.notifications;

import com.backend.housing.domain.entity.notifications.valueobjects.NotificationId;

public interface MarkNotificationAsReadUseCase {
    void execute(NotificationId notificationId, Long userId);
}