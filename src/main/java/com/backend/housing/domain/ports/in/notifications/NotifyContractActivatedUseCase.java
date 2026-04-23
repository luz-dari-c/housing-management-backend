package com.backend.housing.domain.ports.in.notifications;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface NotifyContractActivatedUseCase {
    void execute(Long tenantId, Long ownerId, ContractId contractId);
}