package com.backend.housing.domain.ports.in.notifications;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface NotifyPaymentSucceededUseCase {
    void execute(Long userId, ContractId contractId, boolean isTenant);
}