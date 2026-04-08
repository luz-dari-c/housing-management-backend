package com.backend.housing.domain.ports.out.rentalcontracts;


import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

import java.util.List;
import java.util.Optional;

public interface RentalContractRepository {

    RentalContract save(RentalContract contract);
    Optional<RentalContract> findById(ContractId id);
    List<RentalContract> findByTenantId(Long tenantId);
    List<RentalContract> findByOwnerId(Long ownerId);
    List<RentalContract> findByPropertyId(PropertyId propertyId);
    Optional<RentalContract> findActiveByPropertyId(PropertyId propertyId);
    boolean existsActiveByPropertyId(PropertyId propertyId);
    List<RentalContract> findActiveContracts();
}
