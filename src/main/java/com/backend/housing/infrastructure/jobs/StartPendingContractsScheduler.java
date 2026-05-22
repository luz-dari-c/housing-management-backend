package com.backend.housing.infrastructure.jobs;

import com.backend.housing.domain.ports.in.jobs.StartPendingContractsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StartPendingContractsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StartPendingContractsScheduler.class);
    private final StartPendingContractsUseCase startPendingContractsUseCase;

    public StartPendingContractsScheduler(StartPendingContractsUseCase startPendingContractsUseCase) {
        this.startPendingContractsUseCase = startPendingContractsUseCase;
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "America/Bogota")
    public void startPendingContracts() {
        logger.info("Iniciando job de contratos pendientes");
        try {
            startPendingContractsUseCase.execute();
            logger.info("Job de contratos pendientes completado exitosamente");
        } catch (Exception e) {
            logger.error("Error en job de contratos pendientes: {}", e.getMessage(), e);
        }
    }
}