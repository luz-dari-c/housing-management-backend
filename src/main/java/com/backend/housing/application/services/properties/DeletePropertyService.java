package com.backend.housing.application.services.properties;

import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.valueObjects.PropertyId;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.exceptions.InvalidNotFoundException;
import com.backend.housing.domain.ports.in.properties.DeletePropertyUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public class DeletePropertyService implements DeletePropertyUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeletePropertyService.class);
    private final PropertyRepository propertyRepository;
    private final RentalRequestRepository rentalRequestRepository;


    public DeletePropertyService(PropertyRepository propertyRepository,
                                 RentalRequestRepository rentalRequestRepository) {
        this.propertyRepository = propertyRepository;
        this.rentalRequestRepository = rentalRequestRepository;
    }

    @Override
    public void execute(PropertyId propertyId, Long requestingUserId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new InvalidNotFoundException(
                        "Propiedad no encontrada: " + propertyId
                ));

        if (!property.isOwnedBy(requestingUserId)) {
            throw new SecurityException("No tienes permiso para eliminar esta propiedad");
        }

        property.delete();

        propertyRepository.save(property);

        rejectPendingRequests(propertyId);
    }

    private void rejectPendingRequests(PropertyId propertyId) {
        List<RentalRequest> pendingRequests =
                rentalRequestRepository.findPendingByPropertyId(propertyId);

        if (pendingRequests.isEmpty()) {
            return;
        }

        logger.info("Rechazando {} solicitudes pendientes de la propiedad {}",
                pendingRequests.size(), propertyId);

        pendingRequests.forEach(request -> {
            request.reject();
            rentalRequestRepository.save(request);
        });
    }



}
