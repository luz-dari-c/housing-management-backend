package com.backend.housing.domain.ports.out.payments;

import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;

import java.util.UUID;

public interface ContractPaymentRepository {

    void activateContract(UUID contractId);
}