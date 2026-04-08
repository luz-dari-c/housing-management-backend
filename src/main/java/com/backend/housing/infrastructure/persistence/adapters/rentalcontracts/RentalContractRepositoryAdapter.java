package com.backend.housing.infrastructure.persistence.adapters.rentalcontracts;


import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import com.backend.housing.infrastructure.persistence.entities.rentalcontract.RentalContractEntity;
import com.backend.housing.infrastructure.persistence.mappers.rentalcontracts.ContractEntityMapper;
import com.backend.housing.infrastructure.persistence.repositories.rentalcontracts.JpaRentalContractRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RentalContractRepositoryAdapter implements RentalContractRepository {

    private final JpaRentalContractRepository jpaRepository;
    private final ContractEntityMapper mapper;

    public RentalContractRepositoryAdapter(JpaRentalContractRepository jpaRepository,
                                           ContractEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public RentalContract save(RentalContract contract) {
        RentalContractEntity entity = mapper.toEntity(contract);
        RentalContractEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RentalContract> findById(ContractId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<RentalContract> findByTenantId(Long tenantId) {
        return jpaRepository.findByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalContract> findByOwnerId(Long ownerId) {
        return jpaRepository.findByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalContract> findByPropertyId(PropertyId propertyId) {
        return jpaRepository.findByPropertyId(propertyId.getValue()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RentalContract> findActiveByPropertyId(PropertyId propertyId) {
        return jpaRepository.findByPropertyIdAndStatus(propertyId.getValue(), "ACTIVE")
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsActiveByPropertyId(PropertyId propertyId) {
        return jpaRepository.existsActiveByPropertyId(propertyId.getValue());
    }

    @Override
    public List<RentalContract> findActiveContracts() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}