package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.ports.in.notifications.GetUnreadNotificationsCountUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetUnreadNotificationsCountService implements GetUnreadNotificationsCountUseCase {

    private final NotificationRepository notificationRepository;

    public GetUnreadNotificationsCountService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public int execute(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
}