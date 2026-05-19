package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyContractTerminatedUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyContractTerminatedService implements NotifyContractTerminatedUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyContractTerminatedService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void execute(Long tenantId, Long ownerId, ContractId contractId) {

        Notification tenantNotification = Notification.create(
                tenantId,
                NotificationType.CONTRACT_TERMINATED,
                "Contrato finalizado",
                "El contrato de arriendo ha llegado a su fecha de finalización. Gracias por usar VOLTRIX HOUSE. Esperamos verte pronto de nuevo.",
                contractId
        );
        notificationRepository.save(tenantNotification);

        Notification ownerNotification = Notification.create(
                ownerId,
                NotificationType.CONTRACT_TERMINATED,
                "Contrato finalizado",
                "El contrato de arriendo de tu propiedad ha finalizado. La propiedad ya está disponible para nuevos arrendatarios.",
                contractId
        );
        notificationRepository.save(ownerNotification);
    }
}