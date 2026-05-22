package com.backend.housing.application.services.payments;

import com.backend.housing.application.commands.payments.HandlePaymentWebhookCommand;
import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.events.ContractActivatedEvent;
import com.backend.housing.domain.events.PaymentReceivedEvent;
import com.backend.housing.domain.ports.in.payments.HandlePaymentWebhookUseCase;
import com.backend.housing.domain.ports.out.payments.PaymentRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class HandlePaymentWebhookService implements HandlePaymentWebhookUseCase {

    private final PaymentRepository paymentRepository;
    private final RentalContractRepository contractRepository;
    private final ApplicationEventPublisher eventPublisher;

    public HandlePaymentWebhookService(PaymentRepository paymentRepository,
                                       RentalContractRepository contractRepository,
                                       ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.contractRepository = contractRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(HandlePaymentWebhookCommand command) {

        Payment payment = paymentRepository.findByCheckoutSessionId(command.getCheckoutSessionId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pago no encontrado para la sesión: " + command.getCheckoutSessionId()));

        if (payment.getStatus() == PaymentStatus.SUCCEEDED) {
            return;
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "El pago no puede ser exitoso. Estado actual: " + payment.getStatus());
        }

        payment.markAsSucceeded();
        paymentRepository.save(payment);

        RentalContract contract = contractRepository.findById(ContractId.of(payment.getReferenceId()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contrato no encontrado: " + payment.getReferenceId()));

        if (payment.getReferenceType() == PaymentReferenceType.RENTAL) {
            if (contract.getStatus() == ContractStatus.PAYMENT_PENDING) {
                contract.activate(command.getPaymentConfirmedDate());
                contractRepository.save(contract);
                eventPublisher.publishEvent(new ContractActivatedEvent(
                        contract.getId(),
                        contract.getTenantId(),
                        contract.getOwnerId()
                ));
            } else if (contract.getStatus() == ContractStatus.ACTIVE ||
                    contract.getStatus() == ContractStatus.CANCELLATION_PENDING) {
                contract.renewPaymentPeriod(command.getPaymentConfirmedDate());
                contractRepository.save(contract);
                eventPublisher.publishEvent(new PaymentReceivedEvent(
                        contract.getId(),
                        contract.getTenantId(),
                        contract.getOwnerId(),
                        payment.getPeriod()
                ));
            } else {
                throw new IllegalStateException(
                        "No se puede procesar el pago para el contrato con estado: " + contract.getStatus());
            }
        }
    }
}