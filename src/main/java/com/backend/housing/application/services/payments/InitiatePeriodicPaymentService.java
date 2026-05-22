package com.backend.housing.application.services.payments;

import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentMethod;
import com.backend.housing.domain.entity.payments.enums.PaymentReferenceType;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.payments.InitiatePeriodicPaymentUseCase;
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
public class InitiatePeriodicPaymentService implements InitiatePeriodicPaymentUseCase {

    private final RentalContractRepository contractRepository;
    private final PaymentProviderPort paymentProvider;
    private final PaymentRepository paymentRepository;
    private final PaymentUrlConfig paymentUrlConfig;

    public InitiatePeriodicPaymentService(RentalContractRepository contractRepository,
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
    public String execute(ContractId contractId, Long tenantId) {

        RentalContract contract = getValidatedContract(contractId, tenantId);

        String period = resolveCurrentPeriod(contract);

        ensurePeriodNotAlreadyPaid(contract, period);

        CheckoutSessionResult checkout = createCheckout(contract, contractId);

        persistPayment(contract, checkout, period);

        return checkout.getCheckoutUrl();
    }

    private RentalContract getValidatedContract(ContractId contractId, Long tenantId) {

        RentalContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado: " + contractId));

        if (contract.getStatus() != ContractStatus.ACTIVE &&
                contract.getStatus() != ContractStatus.CANCELLATION_PENDING) {
            throw new IllegalStateException(
                    "Solo los contratos activos pueden ser pagados. Estado actual: " + contract.getStatus());
        }

        if (!contract.getTenantId().equals(tenantId)) {
            throw new SecurityException("Solo el arrendatario puede iniciar el pago de este contrato");
        }

        return contract;
    }

    private String resolveCurrentPeriod(RentalContract contract) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return switch (contract.getPaymentFrequency()) {

            case WEEKLY -> {
                LocalDate weekStart = now.minusDays(now.getDayOfWeek().getValue() - 1);
                yield weekStart.format(yyyyMMdd) + "_week";
            }

            case BIWEEKLY -> {
                int weekOfYear = now.getDayOfYear() / 14;
                yield now.getYear() + "-W" + weekOfYear;
            }

            case MONTHLY ->
                    now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        };
    }

    private void ensurePeriodNotAlreadyPaid(RentalContract contract, String period) {

        boolean exists = paymentRepository.existsByReferenceIdAndPeriod(
                contract.getId().getValue(),
                period
        );

        if (exists) {
            String periodoLegible = formatearPeriodo(period, contract.getPaymentFrequency());
            throw new IllegalStateException("Ya existe un pago registrado para " + periodoLegible + ". No puedes pagar dos veces el mismo período.");
        }
    }

    private String formatearPeriodo(String period, com.backend.housing.domain.entity.properties.enums.PaymentFrequency frequency) {
        if (period == null) return "este período";

        return switch (frequency) {
            case MONTHLY -> {
                if (period.matches("\\d{4}-\\d{2}")) {
                    String[] parts = period.split("-");
                    String monthName = switch (parts[1]) {
                        case "01" -> "Enero";
                        case "02" -> "Febrero";
                        case "03" -> "Marzo";
                        case "04" -> "Abril";
                        case "05" -> "Mayo";
                        case "06" -> "Junio";
                        case "07" -> "Julio";
                        case "08" -> "Agosto";
                        case "09" -> "Septiembre";
                        case "10" -> "Octubre";
                        case "11" -> "Noviembre";
                        case "12" -> "Diciembre";
                        default -> parts[1];
                    };
                    yield monthName + " " + parts[0];
                }
                yield period;
            }
            case WEEKLY -> {
                if (period.contains("_week")) {
                    String date = period.replace("_week", "");
                    yield "la semana que inicia el " + date;
                }
                yield period;
            }
            case BIWEEKLY -> {
                if (period.contains("-W")) {
                    String[] parts = period.split("-W");
                    yield "la quincena " + parts[1] + " del " + parts[0];
                }
                yield period;
            }
        };
    }

    private CheckoutSessionResult createCheckout(RentalContract contract,
                                                 ContractId contractId) {

        BigDecimal amount = contract.getPeriodRent().getAmount();

        return paymentProvider.createCheckoutSession(
                amount,
                "COP",
                contractId.getValue(),
                PaymentReferenceType.RENTAL,
                paymentUrlConfig.getSuccessUrl(),
                paymentUrlConfig.getCancelUrl()
        );
    }

    private void persistPayment(RentalContract contract,
                                CheckoutSessionResult checkout,
                                String period) {

        Payment payment = Payment.createWithCheckoutSession(
                contract.getId().getValue(),
                PaymentReferenceType.RENTAL,
                contract.getPeriodRent().getAmount(),
                "COP",
                PaymentMethod.CARD,
                checkout.getSessionId(),
                checkout.getCheckoutUrl(),
                period
        );

        paymentRepository.save(payment);
    }
}