package com.backend.housing.application.services.jobs;

import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.ports.in.jobs.ProcessPendingCancellationsUseCase;
import com.backend.housing.domain.ports.in.notifications.NotifyContractCancelledUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProcessPendingCancellationsService implements ProcessPendingCancellationsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessPendingCancellationsService.class);

    private final RentalContractRepository contractRepository;
    private final PropertyServicePort propertyService;
    private final NotifyContractCancelledUseCase notifyContractCancelled;

    public ProcessPendingCancellationsService(RentalContractRepository contractRepository,
                                              PropertyServicePort propertyService,
                                              NotifyContractCancelledUseCase notifyContractCancelled) {
        this.contractRepository = contractRepository;
        this.propertyService = propertyService;
        this.notifyContractCancelled = notifyContractCancelled;
    }

    @Override
    public void execute() {
        LocalDate today = LocalDate.now();

        List<RentalContract> pendingCancellations =
                contractRepository.findPendingCancellationsDue(today);

        logger.info("Contratos con cancelación pendiente a procesar: {}", pendingCancellations.size());

        for (RentalContract contract : pendingCancellations) {
            try {
                processCancellation(contract);
            } catch (Exception e) {
                logger.error("Error al procesar cancelación del contrato {}: {}",
                        contract.getId(), e.getMessage(), e);
            }
        }
    }

    private void processCancellation(RentalContract contract) {
        contract.cancel();
        contractRepository.save(contract);
        propertyService.markAsAvailable(contract.getPropertyId());
        notifyContractCancelled.execute(
                contract.getTenantId(),
                contract.getOwnerId(),
                contract.getId(),
                true
        );
        logger.info("Contrato {} cancelado exitosamente", contract.getId());
    }
}