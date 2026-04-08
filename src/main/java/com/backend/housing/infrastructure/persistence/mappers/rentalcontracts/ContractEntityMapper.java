package com.backend.housing.infrastructure.persistence.mappers.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.MonthlyRent;
import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalContractEntity;
import org.springframework.stereotype.Component;

@Component
public class ContractEntityMapper {

    public RentalContractEntity toEntity(RentalContract domain) {
        return new RentalContractEntity(
                domain.getId() != null ? domain.getId().getValue() : null,
                domain.getPropertyId().getValue(),
                domain.getTenantId(),
                domain.getOwnerId(),
                domain.getPeriod().getStartDate(),
                domain.getPeriod().getEndDate(),
                domain.getMonthlyRent().getAmount(),
                domain.getStatus().name(),
                domain.getCreatedAt(),
                domain.getTerminatedAt()
        );
    }

    public RentalContract toDomain(RentalContractEntity entity) {

        ContractId contractId = ContractId.of(entity.getId());
        PropertyId propertyId = PropertyId.of(entity.getPropertyId());
        DateRange period = DateRange.of(entity.getStartDate(), entity.getEndDate());
        MonthlyRent monthlyRent = MonthlyRent.of(entity.getMonthlyRent());
        ContractStatus status = ContractStatus.valueOf(entity.getStatus());

        return RentalContract.reconstitute(
                contractId,
                propertyId,
                entity.getTenantId(),
                entity.getOwnerId(),
                period,
                monthlyRent,
                status,
                entity.getCreatedAt(),
                entity.getTerminatedAt()
        );
    }
}