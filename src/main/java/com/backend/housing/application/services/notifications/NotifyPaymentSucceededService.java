package com.backend.housing.application.services.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.notifications.NotifyPaymentSucceededUseCase;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyPaymentSucceededService implements NotifyPaymentSucceededUseCase {

    private final NotificationRepository notificationRepository;

    public NotifyPaymentSucceededService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public void execute(Long userId, ContractId contractId, boolean isTenant) {
        String title = "Pago recibido";
        String message = isTenant
                ? "Tu pago ha sido recibido exitosamente. Tu contrato está al día hasta la próxima fecha de vencimiento."
                : "El pago del arrendatario ha sido recibido exitosamente.";

        Notification notification = Notification.create(
                userId,
                NotificationType.PAYMENT_SUCCEEDED,
                title,
                message,
                contractId
        );
        notificationRepository.save(notification);
    }
}