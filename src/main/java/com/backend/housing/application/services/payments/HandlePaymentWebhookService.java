package com.backend.housing.application.services.payments;

import com.backend.housing.application.commands.payments.HandlePaymentWebhookCommand;
import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.events.ContractActivatedEvent;
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
                        "Payment not found for session: " + command.getCheckoutSessionId()));

        if (payment.getStatus() == PaymentStatus.SUCCEEDED) {
            return;
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment cannot be succeeded. Current status: " + payment.getStatus());
        }

        RentalContract contract = null;

        if (payment.getReferenceType() == PaymentReferenceType.RENTAL) {
            contract = activateRentalContract(payment.getReferenceId(), command.getPaymentConfirmedDate());
        }

        payment.markAsSucceeded();
        paymentRepository.save(payment);

        if (contract != null) {
            eventPublisher.publishEvent(new ContractActivatedEvent(
                    contract.getId(),
                    contract.getTenantId(),
                    contract.getOwnerId()
            ));
        }
    }

    private RentalContract activateRentalContract(UUID contractId, java.time.LocalDate confirmedDate) {
        RentalContract contract = contractRepository.findById(ContractId.of(contractId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Rental contract not found: " + contractId));

        if (contract.getStatus() != ContractStatus.PAYMENT_PENDING) {
            throw new IllegalStateException(
                    "Contract is not in PAYMENT_PENDING. Current status: " + contract.getStatus());
        }

        contract.activate(confirmedDate);
        return contractRepository.save(contract);
    }
}