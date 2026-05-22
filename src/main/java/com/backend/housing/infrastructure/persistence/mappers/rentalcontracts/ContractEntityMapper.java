package com.backend.housing.infrastructure.persistence.mappers.rentalcontracts;
import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.PeriodRent;
import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalContractEntity;
import org.springframework.stereotype.Component;


@Component
public class ContractEntityMapper {

    public RentalContractEntity toEntity(RentalContract domain) {
        return new RentalContractEntity(
                domain.getId().isEmpty() ? null : domain.getId().getValue(),
                domain.getPropertyId().getValue(),
                domain.getTenantId(),
                domain.getOwnerId(),
                domain.getPeriod().getStartDate(),
                domain.getPeriod().getEndDate(),
                domain.getPeriodRent().getAmount(),
                domain.getPaymentFrequency().name(),
                domain.getStatus().name(),
                domain.getCreatedAt(),
                domain.getTerminatedAt(),
                domain.getActualStartDate(),
                domain.getPaymentDueDate(),
                domain.getEffectiveCancellationDate()
        );
    }

    public RentalContract toDomain(RentalContractEntity entity) {

        ContractId contractId = ContractId.of(entity.getId());
        PropertyId propertyId = PropertyId.of(entity.getPropertyId());
        DateRange period = DateRange.of(entity.getStartDate(), entity.getEndDate());
        PeriodRent periodRent = PeriodRent.of(entity.getPeriodRent());
        ContractStatus status = ContractStatus.valueOf(entity.getStatus());
        PaymentFrequency paymentFrequency =
                PaymentFrequency.valueOf(entity.getPaymentFrequency());

        return RentalContract.reconstitute(
                contractId,
                propertyId,
                entity.getTenantId(),
                entity.getOwnerId(),
                period,
                periodRent,
                paymentFrequency,
                status,
                entity.getCreatedAt(),
                entity.getTerminatedAt(),
                entity.getActualStartDate(),
                entity.getPaymentDueDate(),
                entity.getEffectiveCancellationDate()
        );
    }
}
