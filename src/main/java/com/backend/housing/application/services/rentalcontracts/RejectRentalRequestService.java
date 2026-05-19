package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.in.notifications.NotifyRequestRejectedUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.RejectRentalRequestUseCase;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RejectRentalRequestService implements RejectRentalRequestUseCase {

    private final RentalRequestRepository rentalRequestRepository;
    private final PropertyRepository propertyRepository;
    private final NotifyRequestRejectedUseCase notifyRequestRejectedUseCase;

    public RejectRentalRequestService(RentalRequestRepository rentalRequestRepository,
                                      PropertyRepository propertyRepository,
                                      NotifyRequestRejectedUseCase notifyRequestRejectedUseCase) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.propertyRepository = propertyRepository;
        this.notifyRequestRejectedUseCase = notifyRequestRejectedUseCase;
    }

    @Override
    @Transactional
    public void execute(RequestId requestId, Long ownerId) {

        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud de arriendo no encontrada"));

        if (!request.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("No eres el propietario de esta propiedad");
        }

        if (!request.isPending()) {
            throw new RuntimeException("La solicitud no está en estado pendiente");
        }

        request.reject();
        rentalRequestRepository.save(request);

        notifyRequestRejectedUseCase.execute(request.getTenantId(), requestId);
    }
}