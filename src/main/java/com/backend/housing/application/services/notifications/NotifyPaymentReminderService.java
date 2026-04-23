package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyPaymentReminderUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyPaymentReminderService implements NotifyPaymentReminderUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyPaymentReminderService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void execute(Long tenantId, ContractId contractId, int daysRemaining) {
        Notification notification = Notification.create(
                tenantId,
                NotificationType.PAYMENT_REMINDER,
                "Pago de arriendo próximo",
                "Tu pago de arriendo vence en " + daysRemaining + " días. Realiza el pago para evitar la expiración de tu contrato.",
                contractId
        );
        notificationRepository.save(notification);
    }
}