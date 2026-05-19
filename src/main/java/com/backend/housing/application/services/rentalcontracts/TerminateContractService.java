package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.TerminateContractCommand;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.notifications.NotifyContractTerminatedUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.TerminateContractUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TerminateContractService implements TerminateContractUseCase {

    private final RentalContractRepository contractRepository;
    private final PropertyServicePort propertyService;
    private final UserValidationPort userValidationPort;
    private final NotifyContractTerminatedUseCase notifyContractTerminatedUseCase;


    public TerminateContractService(RentalContractRepository contractRepository,
                                    PropertyServicePort propertyService,
                                    UserValidationPort userValidationPort,
                                    NotifyContractTerminatedUseCase notifyContractTerminatedUseCase) {
        this.contractRepository = contractRepository;
        this.propertyService = propertyService;
        this.userValidationPort = userValidationPort;
        this.notifyContractTerminatedUseCase = notifyContractTerminatedUseCase;
    }

    @Override
    public RentalContract terminateContract(TerminateContractCommand command) {

        User user = getAuthenticatedUser();

        RentalContract contract = contractRepository.findById(command.getContractId())
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + command.getContractId()));

        validateAccess(contract, user.getId());
        validateActiveContract(contract);

        contract.terminate();

        RentalContract terminatedContract = contractRepository.save(contract);

        propertyService.markAsAvailable(contract.getPropertyId());

        notifyContractTerminatedUseCase.execute(
                terminatedContract.getTenantId(),
                terminatedContract.getOwnerId(),
                terminatedContract.getId()
        );
        return terminatedContract;
    }

    private void validateAccess(RentalContract contract, Long userId) {
        if (!contract.belongsToTenant(userId) && !contract.belongsToOwner(userId)) {
            throw new SecurityException("User is not authorized to terminate this contract");
        }
    }

    private void validateActiveContract(RentalContract contract) {
        if (!contract.isActive()) {
            throw new IllegalStateException("Only active contracts can be terminated. Current status: " + contract.getStatus());
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