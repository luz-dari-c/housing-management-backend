package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyContractCancelledUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotifyContractCancelledService implements NotifyContractCancelledUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyContractCancelledService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Override
    public void execute(Long tenantId, Long ownerId, ContractId contractId, boolean immediate) {

        if (immediate) {
            notifyImmediate(tenantId, ownerId, contractId);
        } else {
            notifyScheduled(tenantId, ownerId, contractId);
        }
    }

    private void notifyImmediate(Long tenantId, Long ownerId, ContractId contractId) {
        notificationRepository.save(Notification.create(
                tenantId,
                NotificationType.CONTRACT_CANCELLED,
                "Contrato cancelado",
                "Tu contrato de arriendo ha sido cancelado.",
                contractId
        ));

        notificationRepository.save(Notification.create(
                ownerId,
                NotificationType.CONTRACT_CANCELLED,
                "Contrato cancelado",
                "El contrato de arriendo de tu propiedad ha sido cancelado. La propiedad está disponible nuevamente.",
                contractId
        ));
    }

    private void notifyScheduled(Long tenantId, Long ownerId, ContractId contractId) {
        notificationRepository.save(Notification.create(
                tenantId,
                NotificationType.CONTRACT_CANCELLATION_SCHEDULED,
                "Cancelación de contrato programada",
                "Se ha programado la cancelación de tu contrato. El arriendo seguirá vigente hasta la fecha efectiva de cancelación.",
                contractId
        ));

        notificationRepository.save(Notification.create(
                ownerId,
                NotificationType.CONTRACT_CANCELLATION_SCHEDULED,
                "Cancelación de contrato programada",
                "Se ha solicitado la cancelación del contrato de tu propiedad. El arriendo seguirá vigente hasta la fecha efectiva de cancelación.",
                contractId
        ));
    }
}
