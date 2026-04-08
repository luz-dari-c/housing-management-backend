package com.backend.housing.domain.ports.in.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;

public interface RejectRentalRequestUseCase {
    void execute(RequestId requestId, Long ownerId);
}