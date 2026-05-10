package com.backend.housing.infrastructure.jobs;

import com.backend.housing.domain.ports.in.jobs.ProcessPendingCancellationsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProcessPendingCancellationsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ProcessPendingCancellationsScheduler.class);

    private final ProcessPendingCancellationsUseCase processPendingCancellationsUseCase;

    public ProcessPendingCancellationsScheduler(ProcessPendingCancellationsUseCase processPendingCancellationsUseCase) {
        this.processPendingCancellationsUseCase = processPendingCancellationsUseCase;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void processPendingCancellations() {
        logger.info("Iniciando job de cancelaciones pendientes");
        try {
            processPendingCancellationsUseCase.execute();
            logger.info("Job de cancelaciones pendientes completado exitosamente");
        } catch (Exception e) {
            logger.error("Error en job de cancelaciones pendientes: {}", e.getMessage(), e);
        }
    }
}
