package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.in.rentalcontracts.RejectRentalRequestUseCase;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class RejectRentalRequestService implements RejectRentalRequestUseCase {

    private final RentalRequestRepository rentalRequestRepository;
    private final PropertyRepository propertyRepository;

    public RejectRentalRequestService(RentalRequestRepository rentalRequestRepository,
                                      PropertyRepository propertyRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void execute(RequestId requestId, Long ownerId) {

        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        if (!request.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("You are not the owner");
        }

        if (!request.isPending()) {
            throw new RuntimeException("Request is not pending");
        }

        request.reject();
        rentalRequestRepository.save(request);


    }
}