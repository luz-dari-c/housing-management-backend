package com.backend.housing.application.services.payments;

import com.backend.housing.application.commands.payments.InitiatePaymentCommand;
import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentMethod;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.ports.in.payments.InitiatePaymentUseCase;
import com.backend.housing.domain.ports.out.payments.CheckoutSessionResult;
import com.backend.housing.domain.ports.out.payments.PaymentProviderPort;
import com.backend.housing.domain.ports.out.payments.PaymentRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import com.backend.housing.infrastructure.config.PaymentUrlConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InitiatePaymentService implements InitiatePaymentUseCase {

    private static final DateTimeFormatter PERIOD_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM");

    private final RentalContractRepository contractRepository;
    private final PaymentProviderPort paymentProvider;
    private final PaymentRepository paymentRepository;
    private final PaymentUrlConfig paymentUrlConfig;

    public InitiatePaymentService(RentalContractRepository contractRepository,
                                  PaymentProviderPort paymentProvider,
                                  PaymentRepository paymentRepository,
                                  PaymentUrlConfig paymentUrlConfig) {
        this.contractRepository = contractRepository;
        this.paymentProvider = paymentProvider;
        this.paymentRepository = paymentRepository;
        this.paymentUrlConfig = paymentUrlConfig;
    }

    @Override
    @Transactional
    public String initiatePayment(InitiatePaymentCommand command) {

        RentalContract contract = getValidatedContract(command);

        CheckoutSessionResult checkout = createCheckout(contract);

        String period = resolveCurrentPeriod();

        persistPayment(contract, checkout, period);

        return checkout.getCheckoutUrl();
    }

    private RentalContract getValidatedContract(InitiatePaymentCommand command) {

        RentalContract contract = contractRepository.findById(command.getContractId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Contract not found: " + command.getContractId()));

        if (contract.getStatus() != ContractStatus.PAYMENT_PENDING) {
            throw new IllegalStateException(
                    "Contract is not in PAYMENT_PENDING state. Current state: "
                            + contract.getStatus());
        }

        if (!contract.getTenantId().equals(command.getTenantId())) {
            throw new SecurityException(
                    "Only the tenant can initiate payment for this contract");
        }

        return contract;
    }

    private CheckoutSessionResult createCheckout(RentalContract contract) {

        BigDecimal amount = contract.getMonthlyRent().getAmount();

        return paymentProvider.createCheckoutSession(
                amount,
                "COP",
                contract.getId().getValue(),
                PaymentReferenceType.RENTAL,
                paymentUrlConfig.getSuccessUrl(),
                paymentUrlConfig.getCancelUrl()
        );
    }

    private String resolveCurrentPeriod() {
        return LocalDate.now().format(PERIOD_FORMATTER);
    }

    private void persistPayment(RentalContract contract,
                                CheckoutSessionResult checkout,
                                String period) {

        Payment payment = Payment.createWithCheckoutSession(
                contract.getId().getValue(),
                PaymentReferenceType.RENTAL,
                contract.getMonthlyRent().getAmount(),
                "COP",
                PaymentMethod.CARD,
                checkout.getSessionId(),
                checkout.getCheckoutUrl(),
                period
        );

        paymentRepository.save(payment);
    }
}