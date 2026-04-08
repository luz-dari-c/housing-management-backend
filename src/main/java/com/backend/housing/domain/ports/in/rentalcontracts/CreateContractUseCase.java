package com.backend.housing.domain.ports.in.rentalcontracts;


import com.backend.housing.application.commands.rentalcontracts.CreateContractCommand;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;

public interface CreateContractUseCase {
    RentalContract createContract(CreateContractCommand command);

}
