package com.backend.housing.infrastructure.jobs;

import com.backend.housing.domain.ports.in.jobs.ExpireOverdueContractsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpireContractsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ExpireContractsScheduler.class);
    private final ExpireOverdueContractsUseCase expireOverdueContractsUseCase;

    public ExpireContractsScheduler(ExpireOverdueContractsUseCase expireOverdueContractsUseCase) {
        this.expireOverdueContractsUseCase = expireOverdueContractsUseCase;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void expireOverdueContracts() {
        logger.info("Iniciando job de expiración de contratos vencidos");
        try {
            expireOverdueContractsUseCase.execute();
            logger.info("Job de expiración completado exitosamente");
        } catch (Exception e) {
            logger.error("Error en job de expiración: {}", e.getMessage(), e);
        }
    }
}