package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyContractActivatedUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotifyContractActivatedService implements NotifyContractActivatedUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyContractActivatedService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void execute(Long tenantId, Long ownerId, ContractId contractId) {
        Notification tenantNotification = Notification.create(
                tenantId,
                NotificationType.CONTRACT_ACTIVATED,
                "Contrato activado",
                "Tu contrato ha sido activado exitosamente. La propiedad ya está disponible para ti.",
                contractId
        );
        notificationRepository.save(tenantNotification);

        Notification ownerNotification = Notification.create(
                ownerId,
                NotificationType.CONTRACT_ACTIVATED,
                "Contrato activado",
                "El contrato de tu propiedad ha sido activado. El arrendatario ya puede ocupar la propiedad.",
                contractId
        );
        notificationRepository.save(ownerNotification);
    }
}