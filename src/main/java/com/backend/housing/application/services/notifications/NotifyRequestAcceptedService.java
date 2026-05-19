package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyRequestAcceptedUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyRequestAcceptedService implements NotifyRequestAcceptedUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyRequestAcceptedService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void execute(Long tenantId, ContractId contractId) {
        Notification notification = Notification.create(
                tenantId,
                NotificationType.REQUEST_ACCEPTED,
                "¡Solicitud de arriendo aceptada!",
                "El propietario ha aceptado tu solicitud de arriendo." +
                        " Ya puedes realizar el pago del primer canon para activar el contrato.",
                contractId

        );
        notificationRepository.save(notification);
    }
}