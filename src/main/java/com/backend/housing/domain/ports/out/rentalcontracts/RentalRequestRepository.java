package com.backend.housing.domain.ports.out.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;

import java.util.List;
import java.util.Optional;

public interface RentalRequestRepository {

    RentalRequest save(RentalRequest request);

    Optional<RentalRequest> findById(RequestId id);

    boolean existsPendingByPropertyId(PropertyId propertyId);

    List<RentalRequest> findByOwnerId(Long ownerId);

    List<RentalRequest> findByTenantId(Long tenantId);
}