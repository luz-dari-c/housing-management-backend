package com.backend.housing.application.services.payments;

import com.backend.housing.application.dto.response.payments.PaymentReceiptResponse;
import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.payments.GetPaymentReceiptUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.payments.PaymentRepository;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import com.backend.housing.infrastructure.pdf.PaymentReceiptPdfGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetPaymentReceiptService implements GetPaymentReceiptUseCase {

    private final PaymentRepository paymentRepository;
    private final RentalContractRepository contractRepository;
    private final UserValidationPort userValidationPort;
    private final PropertyServicePort propertyService;
    private final PaymentReceiptPdfGenerator pdfGenerator;

    public GetPaymentReceiptService(PaymentRepository paymentRepository,
                                    RentalContractRepository contractRepository,
                                    UserValidationPort userValidationPort,
                                    PropertyServicePort propertyService,
                                    PaymentReceiptPdfGenerator pdfGenerator) {
        this.paymentRepository = paymentRepository;
        this.contractRepository = contractRepository;
        this.userValidationPort = userValidationPort;
        this.propertyService = propertyService;
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] executeByPaymentId(PaymentId paymentId, Long userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        if (payment.getStatus() != PaymentStatus.SUCCEEDED) {
            throw new IllegalStateException("Payment is not succeeded. Status: " + payment.getStatus());
        }

        RentalContract contract = contractRepository.findById(ContractId.of(payment.getReferenceId()))
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + payment.getReferenceId()));

        validateUserAccess(contract, userId);

        return generateReceipt(payment, contract);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] executeByContractId(UUID contractId, Long userId) {
        RentalContract contract = contractRepository.findById(ContractId.of(contractId))
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + contractId));

        validateUserAccess(contract, userId);

        Payment payment = paymentRepository.findLatestSucceededByReferenceId(contractId)
                .orElseThrow(() -> new IllegalArgumentException("No succeeded payment found for contract: " + contractId));

        return generateReceipt(payment, contract);
    }

    private void validateUserAccess(RentalContract contract, Long userId) {
        if (!contract.getTenantId().equals(userId) && !contract.getOwnerId().equals(userId)) {
            throw new SecurityException("You are not authorized to view this receipt");
        }
    }

    private byte[] generateReceipt(Payment payment, RentalContract contract) {
        String period = payment.getPeriod() != null ? payment.getPeriod() : "—";
        String message = "Pago procesado exitosamente. Este comprobante certifica la transacción.";

        PaymentReceiptResponse receipt = new PaymentReceiptResponse(
                payment.getId().getValue(),
                contract.getId().getValue(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getMethod(),
                payment.getCreatedAt(),
                payment.getPaidAt(),
                payment.getCheckoutUrl(),
                userValidationPort.getUserName(contract.getTenantId()).orElse("—"),
                userValidationPort.getUserName(contract.getOwnerId()).orElse("—"),
                propertyService.getPropertyBasicInfo(contract.getPropertyId())
                        .map(prop -> prop.getTitle())
                        .orElse("—"),
                period,
                message
        );

        return pdfGenerator.generate(receipt);
    }
}