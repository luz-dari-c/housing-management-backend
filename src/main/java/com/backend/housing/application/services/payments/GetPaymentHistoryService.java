package com.backend.housing.application.services.payments;

import com.backend.housing.application.dto.response.payments.PaymentHistoryItem;
import com.backend.housing.application.dto.response.payments.PaymentHistoryResponse;
import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.payments.GetPaymentHistoryUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.payments.PaymentRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetPaymentHistoryService implements GetPaymentHistoryUseCase {

    private final RentalContractRepository contractRepository;
    private final PaymentRepository paymentRepository;
    private final PropertyServicePort propertyService;

    public GetPaymentHistoryService(RentalContractRepository contractRepository,
                                    PaymentRepository paymentRepository,
                                    PropertyServicePort propertyService) {
        this.contractRepository = contractRepository;
        this.paymentRepository = paymentRepository;
        this.propertyService = propertyService;
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentHistoryResponse execute(ContractId contractId, Long userId) {

        RentalContract contract = getAuthorizedContract(contractId, userId);

        String propertyTitle = resolvePropertyTitle(contract);

        List<PaymentHistoryItem> payments = mapPayments(contractId);

        return buildResponse(contract, contractId, propertyTitle, payments);
    }

    private RentalContract getAuthorizedContract(ContractId contractId, Long userId) {

        RentalContract contract = contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Contract not found: " + contractId));

        if (!contract.getTenantId().equals(userId) &&
                !contract.getOwnerId().equals(userId)) {
            throw new SecurityException(
                    "You are not authorized to view this contract's payments");
        }

        return contract;
    }

    private String resolvePropertyTitle(RentalContract contract) {

        return propertyService.getPropertyBasicInfo(contract.getPropertyId())
                .map(property -> property.getTitle())
                .orElse("Unknown property");
    }

    private List<PaymentHistoryItem> mapPayments(ContractId contractId) {

        List<Payment> payments = paymentRepository
                .findByReferenceIdOrderByCreatedAtDesc(contractId.getValue());

        return payments.stream()
                .map(p -> new PaymentHistoryItem(
                        p.getId().getValue().toString(),
                        p.getAmount(),
                        p.getStatus().name(),
                        p.getPaidAt(),
                        p.getPeriod()
                ))
                .toList();
    }

    private PaymentHistoryResponse buildResponse(RentalContract contract,
                                                 ContractId contractId,
                                                 String propertyTitle,
                                                 List<PaymentHistoryItem> payments) {

        return new PaymentHistoryResponse(
                contractId.getValue().toString(),
                propertyTitle,
                contract.getMonthlyRent().getAmount(),
                contract.getPaymentDueDate(),
                payments
        );
    }
}