package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.MonthlyRent;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.in.rentalcontracts.AcceptRentalRequestUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AcceptRentalRequestService implements AcceptRentalRequestUseCase {

    private final RentalRequestRepository rentalRequestRepository;
    private final PropertyRepository propertyRepository;
    private final RentalContractRepository rentalContractRepository;


    public AcceptRentalRequestService(RentalRequestRepository rentalRequestRepository,
                                      PropertyRepository propertyRepository,
                                      RentalContractRepository rentalContractRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.propertyRepository = propertyRepository;
        this.rentalContractRepository = rentalContractRepository;
    }

    @Transactional
    @Override
    public RentalContract execute(RequestId requestId, Long ownerId) {

        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You are not the owner");
        }

        if (!request.isPending()) {
            throw new RuntimeException("Request is not pending");
        }

        if (!property.isAvailableForRent()) {
            throw new RuntimeException("Property is not available for rent");
        }

        request.accept();
        rentalRequestRepository.save(request);

        BigDecimal finalPrice = request.getProposedRent() != null
                ? request.getProposedRent()
                : property.getPriceAmount();

        RentalContract contract = RentalContract.create(
                property.getId(),
                request.getTenantId(),
                property.getOwnerId(),
                request.getPeriod(),
                MonthlyRent.of(finalPrice)
        );

        RentalContract savedContract = rentalContractRepository.save(contract);

        property.markAsRented();
        propertyRepository.save(property);

        return savedContract;
    }
}