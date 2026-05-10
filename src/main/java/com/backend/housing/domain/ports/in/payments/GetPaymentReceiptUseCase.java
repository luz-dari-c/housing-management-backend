package com.backend.housing.domain.ports.in.payments;

import com.backend.housing.application.dto.response.payments.PaymentReceiptResponse;
import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;

import java.util.UUID;

public interface GetPaymentReceiptUseCase {


    byte[] executeByPaymentId(PaymentId paymentId, Long userId);

    byte[] executeByContractId(UUID contractId, Long userId);
}
