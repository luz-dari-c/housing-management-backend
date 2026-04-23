package com.backend.housing.infrastructure.events;

import com.backend.housing.domain.events.ContractActivatedEvent;
import com.backend.housing.domain.ports.in.notifications.NotifyContractActivatedUseCase;
import com.backend.housing.domain.ports.in.notifications.NotifyPaymentSucceededUseCase;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final NotifyContractActivatedUseCase notifyContractActivatedUseCase;
    private final NotifyPaymentSucceededUseCase notifyPaymentSucceededUseCase;

    public NotificationEventListener(NotifyContractActivatedUseCase notifyContractActivatedUseCase,
                                     NotifyPaymentSucceededUseCase notifyPaymentSucceededUseCase) {
        this.notifyContractActivatedUseCase = notifyContractActivatedUseCase;
        this.notifyPaymentSucceededUseCase = notifyPaymentSucceededUseCase;
    }

    @EventListener
    public void handleContractActivated(ContractActivatedEvent event) {
        notifyContractActivatedUseCase.execute(
                event.getTenantId(),
                event.getOwnerId(),
                event.getContractId()
        );

        notifyPaymentSucceededUseCase.execute(
                event.getTenantId(),
                event.getContractId(),
                true
        );

        notifyPaymentSucceededUseCase.execute(
                event.getOwnerId(),
                event.getContractId(),
                false
        );
    }
}