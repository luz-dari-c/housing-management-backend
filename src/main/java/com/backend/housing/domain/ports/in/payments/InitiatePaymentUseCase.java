package com.backend.housing.domain.ports.in.payments;

import com.backend.housing.application.commands.payments.InitiatePaymentCommand;

public interface InitiatePaymentUseCase {
    String initiatePayment(InitiatePaymentCommand command);
}