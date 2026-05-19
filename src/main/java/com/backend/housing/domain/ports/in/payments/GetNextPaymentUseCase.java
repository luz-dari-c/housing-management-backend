package com.backend.housing.domain.ports.in.payments;

import com.backend.housing.application.dto.response.payments.NextPaymentResponse;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface GetNextPaymentUseCase {
    NextPaymentResponse execute(ContractId contractId, Long userId);
}