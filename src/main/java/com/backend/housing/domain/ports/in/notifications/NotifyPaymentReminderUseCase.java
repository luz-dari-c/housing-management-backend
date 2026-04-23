package com.backend.housing.domain.ports.in.notifications;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface NotifyPaymentReminderUseCase {
    void execute(Long tenantId, ContractId contractId, int daysRemaining);
}