package com.backend.housing.domain.ports.in.notifications;

import com.backend.housing.domain.entity.notifications.Notification;

import java.util.List;

public interface GetUserNotificationsUseCase {
    List<Notification> execute(Long userId);
}