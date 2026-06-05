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
import com.backend.housing.domain.ports.out.users.UserRoleServicePort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class CancelContractService implements CancelContractUseCase {

    private static final int OWNER_CANCELLATION_DAYS = 30;

    private final RentalContractRepository contractRepository;
    private final PropertyServicePort propertyService;
    private final UserValidationPort userValidationPort;
    private final UserRoleServicePort userRoleServicePort;
    private final NotifyContractCancelledUseCase notifyContractCancelled;

    public CancelContractService(RentalContractRepository contractRepository,
                                 PropertyServicePort propertyService,
                                 UserValidationPort userValidationPort,
                                 UserRoleServicePort userRoleServicePort,
                                 NotifyContractCancelledUseCase notifyContractCancelled) {
        this.contractRepository = contractRepository;
        this.propertyService = propertyService;
        this.userValidationPort = userValidationPort;
        this.userRoleServicePort = userRoleServicePort;
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

        boolean isOwner = contract.belongsToOwner(user.getId());

        if (isOwner) {
            return scheduleCancellationByOwner(contract);
        } else {
            return scheduleCancellationByTenant(contract);
        }
    }


    private RentalContract cancelImmediately(RentalContract contract) {
        contract.cancelImmediately();
        RentalContract saved = contractRepository.save(contract);
        propertyService.markAsAvailable(contract.getPropertyId());
        notifyContractCancelled.execute(contract.getTenantId(), contract.getOwnerId(), contract.getId(), true);
        return saved;
    }

    private RentalContract scheduleCancellationByOwner(RentalContract contract) {
        LocalDate effectiveDate = LocalDate.now().plusDays(OWNER_CANCELLATION_DAYS);
        contract.scheduleCancellation(effectiveDate);
        RentalContract saved = contractRepository.save(contract);
        notifyContractCancelled.execute(contract.getTenantId(), contract.getOwnerId(), contract.getId(), false);
        return saved;
    }

    private RentalContract scheduleCancellationByTenant(RentalContract contract) {
        LocalDate effectiveDate = calculateEffectiveCancellationDateForTenant(contract);
        contract.scheduleCancellation(effectiveDate);
        RentalContract saved = contractRepository.save(contract);
        notifyContractCancelled.execute(contract.getTenantId(), contract.getOwnerId(), contract.getId(), false);
        return saved;
    }

    private LocalDate calculateEffectiveCancellationDateForTenant(RentalContract contract) {
        LocalDate today = LocalDate.now();
        LocalDate currentPeriodEnd = contract.getPaymentDueDate();

        if (currentPeriodEnd == null) {
            return today.plusDays(OWNER_CANCELLATION_DAYS);
        }

        long daysToCurrentEnd = ChronoUnit.DAYS.between(today, currentPeriodEnd);

        if (daysToCurrentEnd <= OWNER_CANCELLATION_DAYS) {
            return currentPeriodEnd;
        }

        LocalDate targetDate = today.plusDays(OWNER_CANCELLATION_DAYS);

        LocalDate nextPeriodEnd = currentPeriodEnd;
        while (nextPeriodEnd.isBefore(targetDate)) {
            nextPeriodEnd = switch (contract.getPaymentFrequency()) {
                case MONTHLY -> nextPeriodEnd.plusMonths(1);
                case BIWEEKLY -> nextPeriodEnd.plusWeeks(2);
                case WEEKLY -> nextPeriodEnd.plusWeeks(1);
            };
        }

        return nextPeriodEnd;
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