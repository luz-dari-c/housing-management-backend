package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.in.rentalcontracts.CancelRentalRequestUseCase;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CancelRentalRequestService implements CancelRentalRequestUseCase {

    private final RentalRequestRepository rentalRequestRepository;

    public CancelRentalRequestService(RentalRequestRepository rentalRequestRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
    }

    @Override
    public void execute(RequestId requestId, Long tenantId) {

        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud de arriendo no encontrada: " + requestId));

        if (!request.getTenantId().equals(tenantId)) {
            throw new SecurityException("No tienes permiso para cancelar esta solicitud");
        }

        if (!request.isPending()) {
            throw new IllegalStateException(
                    "Solo se pueden cancelar solicitudes en estado PENDING. Estado actual: " + request.getStatus()
            );
        }

        request.cancel();
        rentalRequestRepository.save(request);
    }
}