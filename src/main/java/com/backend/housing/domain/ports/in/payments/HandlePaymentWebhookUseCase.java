package com.backend.housing.domain.ports.in.payments;

import com.backend.housing.application.commands.payments.HandlePaymentWebhookCommand;

public interface HandlePaymentWebhookUseCase {
    void execute(HandlePaymentWebhookCommand command);
}