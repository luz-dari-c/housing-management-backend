package com.backend.housing.application.services.jobs;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.jobs.ExpireOverdueContractsUseCase;
import com.backend.housing.domain.ports.in.notifications.NotifyContractExpiredUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpireOverdueContractsService implements ExpireOverdueContractsUseCase {

    private final RentalContractRepository contractRepository;
    private final PropertyRepository propertyRepository;
    private final NotifyContractExpiredUseCase notifyContractExpiredUseCase;

    public ExpireOverdueContractsService(RentalContractRepository contractRepository,
                                         PropertyRepository propertyRepository,
                                         NotifyContractExpiredUseCase notifyContractExpiredUseCase) {
        this.contractRepository = contractRepository;
        this.propertyRepository = propertyRepository;
        this.notifyContractExpiredUseCase = notifyContractExpiredUseCase;
    }

    @Override
    @Transactional
    public void execute() {
        LocalDate today = LocalDate.now();
        LocalDate expirationThreshold = today.minusDays(5);

        List<RentalContract> activeContracts = contractRepository.findActiveContracts();

        for (RentalContract contract : activeContracts) {
            LocalDate paymentDueDate = contract.getPaymentDueDate();

            if (paymentDueDate != null && paymentDueDate.isBefore(expirationThreshold)) {
                expireContract(contract);
            }
        }
    }

    private void expireContract(RentalContract contract) {
        ContractId contractId = contract.getId();
        Long tenantId = contract.getTenantId();
        Long ownerId = contract.getOwnerId();
        PropertyId propertyId = contract.getPropertyId();

        contract.expire();
        contractRepository.save(contract);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalStateException("Property not found for expired contract: " + propertyId));

        property.markAsAvailable();
        propertyRepository.save(property);

        notifyContractExpiredUseCase.execute(tenantId, ownerId, contractId);
    }
}