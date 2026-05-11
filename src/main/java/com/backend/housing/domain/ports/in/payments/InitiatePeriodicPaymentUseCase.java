package com.backend.housing.domain.ports.in.payments;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface InitiatePeriodicPaymentUseCase  {
    String execute(ContractId contractId, Long tenantId);
}