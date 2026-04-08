package com.backend.housing.domain.ports.in.rentalcontracts;


import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;

import java.util.UUID;

public interface AcceptRentalRequestUseCase {

    void execute(RequestId requestId, Long ownerId);
}
