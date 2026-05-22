package com.backend.housing.domain.ports.in.rentalcontracts;

import com.backend.housing.application.dto.response.rentalcontracts.CancellationStatusResponse;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface GetCancellationStatusUseCase {
    CancellationStatusResponse execute(ContractId contractId, Long userId);
}