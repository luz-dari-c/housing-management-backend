package com.backend.housing.application.commands.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public class CancelContractCommand {

    private final ContractId contractId;
    private final Long requestingUserId;

    public CancelContractCommand(ContractId contractId, Long requestingUserId) {
        this.contractId = contractId;
        this.requestingUserId = requestingUserId;
    }

    public ContractId getContractId() {
        return contractId;
    }

    public Long getRequestingUserId() {
        return requestingUserId;
    }
}