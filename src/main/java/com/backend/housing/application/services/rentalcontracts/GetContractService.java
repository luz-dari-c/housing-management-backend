package com.backend.housing.application.services.rentalcontracts;

import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.rentalcontracts.GetContractUseCase;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetContractService implements GetContractUseCase {

    private final RentalContractRepository contractRepository;
    private final UserValidationPort userValidationPort;

    public GetContractService(RentalContractRepository contractRepository,
                              UserValidationPort userValidationPort) {
        this.contractRepository = contractRepository;
        this.userValidationPort = userValidationPort;
    }

    @Override
    public RentalContract getContract(ContractId id, Long ignored) {

        User user = getAuthenticatedUser();

        RentalContract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + id));

        validateAccess(contract, user.getId());

        return contract;
    }

    @Override
    public List<RentalContract> getContractsByTenant(Long ignored) {
        Long userId = getAuthenticatedUserId();
        return contractRepository.findByTenantId(userId);
    }

    @Override
    public List<RentalContract> getContractsByOwner(Long ignored) {
        Long userId = getAuthenticatedUserId();
        return contractRepository.findByOwnerId(userId);
    }

    private void validateAccess(RentalContract contract, Long userId) {
        if (!contract.belongsToTenant(userId) && !contract.belongsToOwner(userId)) {
            throw new SecurityException("User is not authorized to view this contract");
        }
    }

    private Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
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