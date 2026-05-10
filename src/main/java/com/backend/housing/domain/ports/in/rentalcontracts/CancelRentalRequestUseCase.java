package com.backend.housing.domain.ports.in.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;

public interface CancelRentalRequestUseCase {
    void execute(RequestId requestId, Long tenantId);
}