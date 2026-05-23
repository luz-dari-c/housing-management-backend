package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.application.dto.response.rentalcontracts.AcceptRequestResponse;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.properties.enums.PaymentFrequency;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.PeriodRent;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.RequestId;
import com.backend.housing.domain.ports.in.notifications.NotifyRequestAcceptedUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.AcceptRentalRequestUseCase;
import com.backend.housing.domain.ports.out.properties.PropertyRepository;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import com.backend.housing.domain.ports.out.users.UserRoleServicePort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AcceptRentalRequestService implements AcceptRentalRequestUseCase {

    private final RentalRequestRepository rentalRequestRepository;
    private final PropertyRepository propertyRepository;
    private final RentalContractRepository rentalContractRepository;
    private final NotifyRequestAcceptedUseCase notifyRequestAcceptedUseCase;
    private final UserRoleServicePort userRoleServicePort;

    public AcceptRentalRequestService(RentalRequestRepository rentalRequestRepository,
                                      PropertyRepository propertyRepository,
                                      RentalContractRepository rentalContractRepository,
                                      NotifyRequestAcceptedUseCase notifyRequestAcceptedUseCase,
                                      UserRoleServicePort userRoleServicePort) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.propertyRepository = propertyRepository;
        this.rentalContractRepository = rentalContractRepository;
        this.notifyRequestAcceptedUseCase = notifyRequestAcceptedUseCase;
        this.userRoleServicePort = userRoleServicePort;
    }

    @Transactional
    @Override
    public AcceptRequestResponse execute(RequestId requestId, Long ownerId) {

        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud de arriendo no encontrada"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        PaymentFrequency paymentFrequency = property.getRentalTerms().getPaymentFrequency();

        if (!property.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("No eres el propietario de esta propiedad");
        }

        if (!request.isPending()) {
            throw new RuntimeException("La solicitud no está en estado pendiente");
        }

        if (!property.isAvailableForRent()) {
            throw new RuntimeException("La propiedad no está disponible para arriendo");
        }

        request.accept();
        rentalRequestRepository.save(request);

        BigDecimal finalPrice = request.getProposedRent() != null
                ? request.getProposedRent()
                : property.getPriceAmount();

        RentalContract contract = RentalContract.create(
                property.getId(),
                request.getTenantId(),
                property.getOwnerId(),
                request.getPeriod().getStartDate(),
                request.getPeriod().getEndDate(),
                finalPrice,
                paymentFrequency
        );

        RentalContract savedContract = rentalContractRepository.save(contract);

        try {
            userRoleServicePort.assignTenantRole(savedContract.getTenantId());
        } catch (Exception ignored) {
        }

        property.markAsRented();
        propertyRepository.save(property);

        notifyRequestAcceptedUseCase.execute(savedContract.getTenantId(), savedContract.getId());

        String message = "Solicitud de arriendo aceptada exitosamente";
        String nextStep = "El contrato ha sido creado. El arrendatario debe realizar el pago del primer canon para activar el contrato.";

        return new AcceptRequestResponse(
                savedContract.getId().getValue(),
                message,
                nextStep
        );
    }
}