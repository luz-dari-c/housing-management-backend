package com.backend.housing.application.services.payments;

import com.backend.housing.application.dto.response.payments.PaymentReceiptResponse;
import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.payments.GetPaymentReceiptUseCase;
import com.backend.housing.domain.ports.out.payments.PaymentRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import com.backend.housing.domain.valueobjects.Pagination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GetPaymentReceiptService implements GetPaymentReceiptUseCase {

    private final PaymentRepository paymentRepository;
    private final RentalContractRepository contractRepository;

    public GetPaymentReceiptService(PaymentRepository paymentRepository,
                                    RentalContractRepository contractRepository) {
        this.paymentRepository = paymentRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentReceiptResponse execute(UUID contractId, Long tenantId) {
        RentalContract contract = contractRepository.findById(ContractId.of(contractId))
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + contractId));

        if (!contract.getTenantId().equals(tenantId)) {
            throw new SecurityException("You are not the tenant of this contract");
        }

        List<Payment> payments = paymentRepository.findByReferenceId(contractId, new Pagination(0, 10));

        Payment payment = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCEEDED)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No succeeded payment found for contract: " + contractId));

        return new PaymentReceiptResponse(
                payment.getId().getValue(),
                contractId,
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getMethod(),
                payment.getCreatedAt(),
                payment.getPaidAt(),
                payment.getCheckoutUrl()
        );
    }
}