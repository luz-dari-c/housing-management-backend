package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CancelContractCommand;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.notifications.NotifyContractCancelledUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.CancelContractUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CancelContractService implements CancelContractUseCase {

    private static final int MINIMUM_NOTICE_DAYS = 30;

    private final RentalContractRepository contractRepository;
    private final PropertyServicePort propertyService;
    private final UserValidationPort userValidationPort;
    private final NotifyContractCancelledUseCase notifyContractCancelled;

    public CancelContractService(RentalContractRepository contractRepository,
                                 PropertyServicePort propertyService,
                                 UserValidationPort userValidationPort,
                                 NotifyContractCancelledUseCase notifyContractCancelled) {
        this.contractRepository = contractRepository;
        this.propertyService = propertyService;
        this.userValidationPort = userValidationPort;
        this.notifyContractCancelled = notifyContractCancelled;
    }

    @Override
    public RentalContract execute(CancelContractCommand command) {

        User user = getAuthenticatedUser();

        RentalContract contract = contractRepository.findById(command.getContractId())
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado: " + command.getContractId()));

        validateAccess(contract, user.getId());
        validateCancellable(contract);

        if (contract.getStatus() == ContractStatus.PAYMENT_PENDING) {
            return cancelImmediately(contract);
        }

        return scheduleCancellation(contract);
    }

    // Contrato aún no ha tenido pagos — se cancela de forma inmediata
    private RentalContract cancelImmediately(RentalContract contract) {
        contract.cancel();
        RentalContract saved = contractRepository.save(contract);
        propertyService.markAsAvailable(contract.getPropertyId());
        notifyContractCancelled.execute(contract.getTenantId(), contract.getOwnerId(), contract.getId(), true);
        return saved;
    }

    // Contrato activo — requiere preaviso de 30 días, queda en CANCELLATION_PENDING
    private RentalContract scheduleCancellation(RentalContract contract) {
        LocalDate effectiveDate = calculateEffectiveCancellationDate(contract);
        contract.scheduleCancellation(effectiveDate);
        RentalContract saved = contractRepository.save(contract);
        notifyContractCancelled.execute(contract.getTenantId(), contract.getOwnerId(), contract.getId(), false);
        return saved;
    }

    /**
     * Calcula la fecha efectiva de cancelación usando la Opción 3:
     * primer payment_due_date posterior a (hoy + 30 días).
     * Garantiza cortes de pago limpios y respeta el preaviso mínimo.
     */
    private LocalDate calculateEffectiveCancellationDate(RentalContract contract) {
        LocalDate minimumDate = LocalDate.now().plusDays(MINIMUM_NOTICE_DAYS);
        LocalDate paymentDueDate = contract.getPaymentDueDate();

        // Avanzar mes a mes hasta encontrar el primer vencimiento posterior al mínimo
        while (!paymentDueDate.isAfter(minimumDate)) {
            paymentDueDate = paymentDueDate.plusMonths(1);
        }

        return paymentDueDate;
    }

    private void validateAccess(RentalContract contract, Long userId) {
        if (!contract.belongsToTenant(userId) && !contract.belongsToOwner(userId)) {
            throw new SecurityException("No tienes permiso para cancelar este contrato");
        }
    }

    private void validateCancellable(RentalContract contract) {
        if (contract.getStatus() != ContractStatus.PAYMENT_PENDING
                && contract.getStatus() != ContractStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Solo se pueden cancelar contratos en estado PAYMENT_PENDING o ACTIVE. Estado actual: "
                            + contract.getStatus()
            );
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = authentication.getName();

        return userValidationPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
