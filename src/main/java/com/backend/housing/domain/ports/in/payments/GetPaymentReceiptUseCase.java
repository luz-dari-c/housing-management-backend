package com.backend.housing.domain.ports.in.payments;

import com.backend.housing.application.dto.response.payments.PaymentReceiptResponse;
import java.util.UUID;

public interface GetPaymentReceiptUseCase {
    PaymentReceiptResponse execute(UUID contractId, Long tenantId);
}
