package com.backend.housing.infrastructure.persistence.repositories.rentalcontracts;

import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaRentalRequestRepository extends JpaRepository<RentalRequestEntity, String> {

    boolean existsByPropertyIdAndStatus(String propertyId, String status);

    List<RentalRequestEntity> findByOwnerId(Long ownerId);

    List<RentalRequestEntity> findByTenantId(Long tenantId);
}