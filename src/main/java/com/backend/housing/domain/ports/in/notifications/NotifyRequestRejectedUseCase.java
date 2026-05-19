package com.backend.housing.domain.ports.in.notifications;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;

public interface NotifyRequestRejectedUseCase {
    void execute(Long tenantId, RequestId requestId);
}