package com.backend.housing.domain.events;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public class ContractActivatedEvent {

    private final ContractId contractId;
    private final Long tenantId;
    private final Long ownerId;

    public ContractActivatedEvent(ContractId contractId, Long tenantId, Long ownerId) {
        this.contractId = contractId;
        this.tenantId = tenantId;
        this.ownerId = ownerId;
    }

    public ContractId getContractId() {
        return contractId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Long getOwnerId() {
        return ownerId;
    }
}