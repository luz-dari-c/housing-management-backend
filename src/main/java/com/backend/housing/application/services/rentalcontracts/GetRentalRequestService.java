package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.in.rentalcontracts.GetRentalRequestUseCase;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetRentalRequestService implements GetRentalRequestUseCase {

    private final RentalRequestRepository rentalRequestRepository;

    public GetRentalRequestService(RentalRequestRepository rentalRequestRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
    }

    @Override
    public RentalRequest getRequestById(UUID requestId, Long userId) {
        RequestId id = RequestId.of(requestId.toString());
        RentalRequest request = rentalRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        if (!request.getOwnerId().equals(userId) && !request.getTenantId().equals(userId)) {
            throw new RuntimeException("Usuario no autorizado para ver esta solicitud");
        }

        return request;
    }

    @Override
    public List<RentalRequest> getRequestsByTenant(Long tenantId) {
        return rentalRequestRepository.findByTenantId(tenantId);
    }

    @Override
    public List<RentalRequest> getRequestsByOwner(Long ownerId) {
        return rentalRequestRepository.findByOwnerId(ownerId);
    }
}