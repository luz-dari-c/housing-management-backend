package com.backend.housing.infrastructure.persistence.adapters.rentalcontracts;

import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalRequestEntity;
import com.backend.housing.infrastructure.persistence.mappers.rentalcontracts.RentalRequestEntityMapper;
import com.backend.housing.infrastructure.persistence.repositories.rentalcontracts.JpaRentalRequestRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RentalRequestRepositoryAdapter implements RentalRequestRepository {

    private final JpaRentalRequestRepository jpaRepository;
    private final RentalRequestEntityMapper mapper;

    public RentalRequestRepositoryAdapter(JpaRentalRequestRepository jpaRepository,
                                          RentalRequestEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public RentalRequest save(RentalRequest request) {
        RentalRequestEntity entity = mapper.toEntity(request);
        RentalRequestEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RentalRequest> findById(RequestId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsPendingByPropertyId(PropertyId propertyId) {
        return jpaRepository.existsByPropertyIdAndStatus(
                propertyId.getValue().toString(),
                "PENDING"
        );
    }


    @Override
    public List<RentalRequest> findByOwnerId(Long ownerId) {
        return jpaRepository.findByOwnerId(ownerId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalRequest> findByTenantId(Long tenantId) {
        return jpaRepository.findByTenantId(tenantId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}