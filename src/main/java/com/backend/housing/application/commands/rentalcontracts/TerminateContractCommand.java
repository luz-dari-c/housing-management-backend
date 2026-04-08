package com.backend.housing.application.commands.rentalcontracts;


import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TerminateContractCommand {
    private final ContractId contractId;
    private final Long requestingUserId;

    public TerminateContractCommand(ContractId contractId, Long requestingUserId) {
        this.contractId = Objects.requireNonNull(contractId, "ContractId is required");
        this.requestingUserId = Objects.requireNonNull(requestingUserId, "RequestingUserId is required");
    }


}