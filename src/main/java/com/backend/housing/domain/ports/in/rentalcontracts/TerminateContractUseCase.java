package com.backend.housing.domain.ports.in.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.TerminateContractCommand;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;


public interface TerminateContractUseCase {

    RentalContract terminateContract(TerminateContractCommand command);

}
