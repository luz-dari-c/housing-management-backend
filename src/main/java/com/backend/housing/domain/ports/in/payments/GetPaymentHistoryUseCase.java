package com.backend.housing.domain.ports.in.payments;


import com.backend.housing.application.dto.response.payments.PaymentHistoryResponse;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

public interface GetPaymentHistoryUseCase {

    PaymentHistoryResponse execute(ContractId id, Long userId);
}
