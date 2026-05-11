package com.backend.housing.domain.events;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public class PaymentReceivedEvent {

    private final ContractId contractId;
    private final Long tenantId;
    private final Long ownerId;
    private final String period;

    public PaymentReceivedEvent(ContractId contractId, Long tenantId, Long ownerId, String period) {
        this.contractId = contractId;
        this.tenantId = tenantId;
        this.ownerId = ownerId;
        this.period = period;
    }

    public ContractId getContractId() { return contractId; }
    public Long getTenantId() { return tenantId; }
    public Long getOwnerId() { return ownerId; }
    public String getPeriod() { return period; }
}