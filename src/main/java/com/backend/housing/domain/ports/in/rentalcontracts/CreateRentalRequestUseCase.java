package com.backend.housing.domain.ports.in.rentalcontracts;


import com.backend.housing.application.commands.rentalcontracts.CreateRentalRequestCommand;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;

public interface CreateRentalRequestUseCase {
    RentalRequest create(CreateRentalRequestCommand command);
}