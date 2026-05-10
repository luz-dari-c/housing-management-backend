package com.backend.housing.domain.ports.in.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CancelContractCommand;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;

public interface CancelContractUseCase {
    RentalContract execute(CancelContractCommand command);
}