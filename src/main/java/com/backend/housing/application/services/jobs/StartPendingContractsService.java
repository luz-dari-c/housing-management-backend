package com.backend.housing.application.services.jobs;

import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.Enums.ContractStatus;
import com.backend.housing.domain.ports.in.jobs.StartPendingContractsUseCase;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StartPendingContractsService implements StartPendingContractsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(StartPendingContractsService.class);
    private final RentalContractRepository contractRepository;

    public StartPendingContractsService(RentalContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional
    public void execute() {
        LocalDate today = LocalDate.now();

        List<RentalContract> pendingContracts = contractRepository.findByStatusAndStartDateBeforeOrEqual(
                ContractStatus.PAID_NOT_STARTED, today);

        logger.info("Contratos pendientes de iniciar: {}", pendingContracts.size());

        for (RentalContract contract : pendingContracts) {
            try {
                contract.startContract();
                contractRepository.save(contract);
                logger.info("Contrato {} iniciado exitosamente", contract.getId());
            } catch (Exception e) {
                logger.error("Error al iniciar contrato {}: {}", contract.getId(), e.getMessage(), e);
            }
        }
    }
}