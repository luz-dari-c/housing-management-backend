package com.backend.housing.application.services.payments;

import com.backend.housing.application.dto.response.payments.NextPaymentResponse;
import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
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
import java.time.temporal.ChronoUnit;

@Service
public class GetNextPaymentService implements GetNextPaymentUseCase {

    private final RentalContractRepository contractRepository;
    private final PaymentRepository paymentRepository;
    private final PropertyServicePort propertyService;

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
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + contractId));

        if (!contract.getTenantId().equals(userId) && !contract.getOwnerId().equals(userId)) {
            throw new SecurityException("You are not authorized to view this contract");
        }

        if (contract.getStatus() != ContractStatus.ACTIVE &&
                contract.getStatus() != ContractStatus.CANCELLATION_PENDING) {
            throw new IllegalStateException("Contract is not active. Status: " + contract.getStatus());
        }

        String propertyTitle = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                .map(p -> p.getTitle())
                .orElse("Unknown property");

        LocalDate paymentDueDate = contract.getPaymentDueDate();

        if (paymentDueDate == null) {
            throw new IllegalStateException("Payment due date not set for this contract");
        }

        LocalDate today = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(today, paymentDueDate);
        boolean isOverdue = daysRemaining < 0;

        String frequencyText = switch (contract.getPaymentFrequency()) {
            case MONTHLY -> "Mensual";
            case BIWEEKLY -> "Quincenal";
            case WEEKLY -> "Semanal";
        };

        return new NextPaymentResponse(
                contract.getId().getValue(),
                propertyTitle,
                contract.getMonthlyRent().getAmount(),
                paymentDueDate,
                (int) Math.abs(daysRemaining),
                frequencyText,
                isOverdue
        );
    }
}