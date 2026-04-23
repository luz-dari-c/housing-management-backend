package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyContractExpiredUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyContractExpiredService implements NotifyContractExpiredUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyContractExpiredService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void execute(Long tenantId, Long ownerId, ContractId contractId) {
        Notification tenantNotification = Notification.create(
                tenantId,
                NotificationType.CONTRACT_EXPIRED,
                "Contrato expirado",
                "Tu contrato ha sido expirado por falta de pago. La propiedad ya no está disponible para ti.",
                contractId
        );
        notificationRepository.save(tenantNotification);

        Notification ownerNotification = Notification.create(
                ownerId,
                NotificationType.CONTRACT_EXPIRED,
                "Contrato expirado - Propiedad liberada",
                "El contrato de tu propiedad ha expirado porque el arrendatario no realizó el pago. Tu propiedad ya está disponible para nuevos arrendatarios.",
                contractId
        );
        notificationRepository.save(ownerNotification);
    }
}