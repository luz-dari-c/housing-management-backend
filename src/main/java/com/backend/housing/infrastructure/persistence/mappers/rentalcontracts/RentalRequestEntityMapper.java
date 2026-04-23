package com.backend.housing.infrastructure.persistence.mappers.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.Enums.RentalRequestStatus;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.DateRange;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalRequestEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RentalRequestEntityMapper {

    public RentalRequestEntity toEntity(RentalRequest domain) {
        return new RentalRequestEntity(
                domain.getId().getValue(),
                domain.getPropertyId().getValue().toString(),
                domain.getTenantId(),
                domain.getOwnerId(),
                domain.getPeriod().getStartDate().atStartOfDay(),
                domain.getPeriod().getEndDate().atStartOfDay(),
                domain.getProposedRent(),
                domain.getStatus().name(),
                domain.getCreatedAt(),
                domain.getRespondedAt()
        );
    }

    public RentalRequest toDomain(RentalRequestEntity entity) {
        return RentalRequest.fromPersistence(
                new RequestId(entity.getId()),
                new PropertyId(UUID.fromString(entity.getPropertyId())),
                entity.getTenantId(),
                entity.getOwnerId(),
                new DateRange(
                        entity.getStartDate().toLocalDate(),
                        entity.getEndDate().toLocalDate()
                ),
                entity.getProposedRent(),
                RentalRequestStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getRespondedAt()
        );
    }
}