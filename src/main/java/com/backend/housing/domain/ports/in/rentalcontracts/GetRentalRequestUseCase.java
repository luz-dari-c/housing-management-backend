package com.backend.housing.domain.ports.in.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;

import java.util.List;
import java.util.UUID;

public interface GetRentalRequestUseCase {

    RentalRequest getRequestById(UUID requestId, Long userId);

    List<RentalRequest> getRequestsByTenant(Long tenantId);

    List<RentalRequest> getRequestsByOwner(Long ownerId);
}