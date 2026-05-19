package com.backend.housing.domain.ports.in.rentalcontracts;

import com.backend.housing.application.dto.response.rentalcontracts.AcceptRequestResponse;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;

public interface AcceptRentalRequestUseCase {
    AcceptRequestResponse execute(RequestId requestId, Long ownerId);
}