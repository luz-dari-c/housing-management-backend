package com.backend.housing.domain.ports.in.notifications;


import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface NotifyRequestSendUseCase {
    void execute(Long tenantId);
}
