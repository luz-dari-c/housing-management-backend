package com.backend.housing.infrastructure.persistence.repositories.rentalcontracts;

import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaRentalRequestRepository extends JpaRepository<RentalRequestEntity, String> {

    boolean existsByPropertyIdAndStatus(String propertyId, String status);

    List<RentalRequestEntity> findByOwnerId(Long ownerId);

    List<RentalRequestEntity> findByTenantId(Long tenantId);

    @Query("SELECT r FROM RentalRequestEntity r " +
            "WHERE r.propertyId = :propertyId " +
            "AND r.status = 'PENDING'")
    List<RentalRequestEntity> findPendingByPropertyId(
            @Param("propertyId") String propertyId
    );
}