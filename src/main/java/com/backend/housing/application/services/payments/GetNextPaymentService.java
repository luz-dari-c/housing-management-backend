package com.backend.housing.application.services.payments;

import com.backend.housing.application.dto.response.payments.NextPaymentResponse;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.payments.GetNextPaymentUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.payments.PaymentRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class GetNextPaymentService implements GetNextPaymentUseCase {

    private final RentalContractRepository contractRepository;
    private final PaymentRepository paymentRepository;
    private final PropertyServicePort propertyService;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "CO"));

    public GetNextPaymentService(RentalContractRepository contractRepository,
                                 PaymentRepository paymentRepository,
                                 PropertyServicePort propertyService) {
        this.contractRepository = contractRepository;
        this.paymentRepository = paymentRepository;
        this.propertyService = propertyService;
    }

    @Override
    @Transactional(readOnly = true)
    public NextPaymentResponse execute(ContractId contractId, Long userId) {

        RentalContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado: " + contractId));

        if (!contract.getTenantId().equals(userId) && !contract.getOwnerId().equals(userId)) {
            throw new SecurityException("No tienes permiso para ver este contrato");
        }

        String propertyTitle = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                .map(p -> p.getTitle())
                .orElse("Propiedad no especificada");

        // (pago pero aun no empieza)
        if (contract.getStatus() == ContractStatus.PAID_NOT_STARTED) {
            LocalDate startDate = contract.getPeriod().getStartDate();
            long daysUntilStart = ChronoUnit.DAYS.between(LocalDate.now(), startDate);

            return new NextPaymentResponse(
                    contract.getId().getValue(),
                    propertyTitle,
                    contract.getPeriodRent().getAmount(),
                    startDate,
                    (int) daysUntilStart,
                    getFrequencyText(contract.getPaymentFrequency()),
                    false,
                    false,
                    "El contrato comenzará el " + startDate.format(DATE_FORMATTER),
                    contract.getPeriodRent().getAmount()
            );
        }

        if (contract.getStatus() != ContractStatus.ACTIVE &&
                contract.getStatus() != ContractStatus.CANCELLATION_PENDING) {
            throw new IllegalStateException("El contrato no está activo. Estado actual: " + contract.getStatus());
        }

        LocalDate paymentDueDate = contract.getPaymentDueDate();
        if (paymentDueDate == null) {
            throw new IllegalStateException("El contrato no tiene fecha de pago configurada");
        }

        LocalDate today = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(today, paymentDueDate);
        boolean isOverdue = daysRemaining < 0;

        String frequencyText = getFrequencyText(contract.getPaymentFrequency());

        boolean canPayNextPeriod = canPayNextPeriod(contract, today);
        String nextPeriodDescription = getNextPeriodDescription(contract, today);
        java.math.BigDecimal nextPeriodAmount = contract.getPeriodRent().getAmount();

        return new NextPaymentResponse(
                contract.getId().getValue(),
                propertyTitle,
                contract.getPeriodRent().getAmount(),
                paymentDueDate,
                (int) Math.abs(daysRemaining),
                frequencyText,
                isOverdue,
                canPayNextPeriod,
                nextPeriodDescription,
                nextPeriodAmount
        );
    }

    private String getFrequencyText(com.backend.housing.domain.entity.properties.enums.PaymentFrequency frequency) {
        return switch (frequency) {
            case MONTHLY -> "Mensual";
            case BIWEEKLY -> "Quincenal";
            case WEEKLY -> "Semanal";
        };
    }

    private boolean canPayNextPeriod(RentalContract contract, LocalDate today) {
        LocalDate paymentDueDate = contract.getPaymentDueDate();
        if (paymentDueDate == null) return false;

        String currentPeriod = getCurrentPeriod(contract, paymentDueDate);
        boolean currentPeriodPaid = paymentRepository.existsByReferenceIdAndPeriod(
                contract.getId().getValue(), currentPeriod);

        if (!currentPeriodPaid) {
            return false;
        }

        int advanceDays = switch (contract.getPaymentFrequency()) {
            case MONTHLY -> 10;
            case BIWEEKLY -> 5;
            case WEEKLY -> 3;
        };

        LocalDate enableDate = paymentDueDate.minusDays(advanceDays);
        return !today.isBefore(enableDate);
    }

    private String getCurrentPeriod(RentalContract contract, LocalDate paymentDueDate) {
        return switch (contract.getPaymentFrequency()) {
            case MONTHLY -> paymentDueDate.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            case BIWEEKLY -> {
                int weekOfYear = paymentDueDate.minusWeeks(2).getDayOfYear() / 14;
                yield paymentDueDate.getYear() + "-W" + weekOfYear;
            }
            case WEEKLY -> {
                LocalDate weekStart = paymentDueDate.minusWeeks(1).minusDays(6);
                yield weekStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "_week";
            }
        };
    }

    private String getNextPeriodDescription(RentalContract contract, LocalDate today) {
        LocalDate paymentDueDate = contract.getPaymentDueDate();
        if (paymentDueDate == null) return "Próximo período";

        return switch (contract.getPaymentFrequency()) {
            case MONTHLY -> {
                LocalDate nextMonthDate = paymentDueDate.plusMonths(1);
                yield nextMonthDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "CO")));
            }
            case BIWEEKLY -> {
                LocalDate nextQuincenaStart = paymentDueDate.plusDays(1);
                LocalDate nextQuincenaEnd = nextQuincenaStart.plusDays(14);
                yield nextQuincenaStart.format(DATE_FORMATTER) + " - " + nextQuincenaEnd.format(DATE_FORMATTER);
            }
            case WEEKLY -> {
                LocalDate nextWeekStart = paymentDueDate.plusDays(1);
                LocalDate nextWeekEnd = nextWeekStart.plusDays(6);
                yield nextWeekStart.format(DATE_FORMATTER) + " - " + nextWeekEnd.format(DATE_FORMATTER);
            }
        };
    }
}