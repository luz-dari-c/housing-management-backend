package com.backend.housing.infrastructure.persistence.repositories.rentalcontracts;


import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRentalContractRepository extends JpaRepository<RentalContractEntity, UUID> {

    List<RentalContractEntity> findByTenantId(Long tenantId);

    List<RentalContractEntity> findByOwnerId(Long ownerId);

    List<RentalContractEntity> findByPropertyId(UUID propertyId);

    Optional<RentalContractEntity> findByPropertyIdAndStatus(UUID propertyId, String status);

    @Query("SELECT COUNT(c) > 0 FROM RentalContractEntity c WHERE c.propertyId = :propertyId AND c.status = 'ACTIVE'")
    boolean existsActiveByPropertyId(@Param("propertyId") UUID propertyId);

    @Query("SELECT c FROM RentalContractEntity c WHERE c.status = 'ACTIVE'")
    List<RentalContractEntity> findAllActive();

    // Contratos cuya cancelación programada ya venció — usados por el job nocturno
    @Query("SELECT c FROM RentalContractEntity c " +
            "WHERE c.status = 'CANCELLATION_PENDING' " +
            "AND c.effectiveCancellationDate <= :date")
    List<RentalContractEntity> findPendingCancellationsDue(@Param("date") LocalDate date);
}
