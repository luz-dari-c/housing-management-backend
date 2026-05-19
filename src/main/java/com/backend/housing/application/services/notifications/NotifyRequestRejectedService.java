package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.in.notifications.NotifyRequestRejectedUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyRequestRejectedService implements NotifyRequestRejectedUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyRequestRejectedService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void execute(Long tenantId, RequestId requestId) {
        Notification notification = Notification.create(
                tenantId,
                NotificationType.REQUEST_REJECTED,
                "Solicitud de arriendo rechazada",
                "El propietario ha rechazado tu solicitud de arriendo. Puedes buscar otras propiedades disponibles.",
                null
        );
        notificationRepository.save(notification);
    }
}