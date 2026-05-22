package com.backend.housing.application.services.notifications;


import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyRequestSendUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyRequestSendService implements NotifyRequestSendUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyRequestSendService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void execute(Long owner) {

        Notification notification = Notification.create(
                owner,
                NotificationType.REQUEST_SEND,
                "Nueva solicitud de arriendo recibida",
                "Un arrendatario ha enviado una solicitud de arriendo para una de tus propiedades. Revisa la solicitud y decide si deseas aprobarla o rechazarla."
        );

        notificationRepository.save(notification);
    }
}
