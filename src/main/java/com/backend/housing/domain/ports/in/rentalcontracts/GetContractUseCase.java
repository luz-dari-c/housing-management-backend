package com.backend.housing.domain.ports.in.rentalcontracts;


import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

import java.util.List;

public interface GetContractUseCase {
        RentalContract getContract(ContractId id, Long requestingUserId);
        List<RentalContract> getContractsByTenant(Long tenantId);
        List<RentalContract> getContractsByOwner(Long ownerId);

}
