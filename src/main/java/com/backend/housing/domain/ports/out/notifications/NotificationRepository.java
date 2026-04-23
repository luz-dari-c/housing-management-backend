package com.backend.housing.domain.ports.out.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.valueobjects.NotificationId;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(NotificationId id);

    List<Notification> findByUserId(Long userId);

    List<Notification> findUnreadByUserId(Long userId);

    int countUnreadByUserId(Long userId);

    void markAsRead(NotificationId id);
}