package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.ports.in.notifications.GetUserNotificationsUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetUserNotificationsService implements GetUserNotificationsUseCase {

    private final NotificationRepository notificationRepository;

    public GetUserNotificationsService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> execute(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}