package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.application.dto.response.rentalcontracts.CancellationStatusResponse;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.ports.in.rentalcontracts.GetCancellationStatusUseCase;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class GetCancellationStatusService implements GetCancellationStatusUseCase {

    private final RentalContractRepository contractRepository;
    private final UserValidationPort userValidationPort;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "CO"));

    public GetCancellationStatusService(RentalContractRepository contractRepository,
                                        UserValidationPort userValidationPort) {
        this.contractRepository = contractRepository;
        this.userValidationPort = userValidationPort;
    }

    @Override
    @Transactional(readOnly = true)
    public CancellationStatusResponse execute(ContractId contractId, Long userId) {

        RentalContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado: " + contractId));

        if (!contract.belongsToTenant(userId) && !contract.belongsToOwner(userId)) {
            throw new SecurityException("No tienes permiso para ver este contrato");
        }

        // Si el contrato está en CANCELLATION_PENDING
        if (contract.getStatus() == ContractStatus.CANCELLATION_PENDING) {
            LocalDate effectiveDate = contract.getEffectiveCancellationDate();
            if (effectiveDate == null) {
                throw new IllegalStateException("El contrato no tiene fecha efectiva de cancelación");
            }

            LocalDate today = LocalDate.now();
            long daysRemaining = ChronoUnit.DAYS.between(today, effectiveDate);
            if (daysRemaining < 0) daysRemaining = 0;

            String cancelledBy = determinarQuienCancelo(contract, userId);
            String message = String.format(
                    "La cancelación se hará efectiva el %s. Faltan %d días.",
                    effectiveDate.format(DATE_FORMATTER), daysRemaining
            );

            return new CancellationStatusResponse(
                    contract.getId().getValue(),
                    contract.getStatus().name(),
                    effectiveDate,
                    daysRemaining,
                    cancelledBy,
                    message
            );
        }

        // Si el contrato ya está cancelado
        if (contract.getStatus() == ContractStatus.CANCELLED) {
            return new CancellationStatusResponse(
                    contract.getId().getValue(),
                    contract.getStatus().name(),
                    contract.getTerminatedAt() != null ? contract.getTerminatedAt().toLocalDate() : null,
                    0,
                    "SISTEMA",
                    "El contrato ya ha sido cancelado."
            );
        }

        // Si el contrato está activo sin cancelación pendiente
        return new CancellationStatusResponse(
                contract.getId().getValue(),
                contract.getStatus().name(),
                null,
                0,
                null,
                "El contrato no tiene ninguna cancelación en proceso."
        );
    }

    private String determinarQuienCancelo(RentalContract contract, Long userId) {
        // Intentar determinar quién solicitó la cancelación
        // Como no guardamos quién canceló, verificamos el rol del usuario actual
        if (contract.belongsToOwner(userId)) {
            return "PROPIETARIO";
        } else if (contract.belongsToTenant(userId)) {
            return "ARRENDATARIO";
        }
        return "DESCONOCIDO";
    }
}