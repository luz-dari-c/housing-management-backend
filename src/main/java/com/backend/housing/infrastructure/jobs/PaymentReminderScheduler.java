package com.backend.housing.infrastructure.jobs;

import com.backend.housing.domain.ports.in.jobs.SendPaymentRemindersUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentReminderScheduler.class);
    private final SendPaymentRemindersUseCase sendPaymentRemindersUseCase;

    public PaymentReminderScheduler(SendPaymentRemindersUseCase sendPaymentRemindersUseCase) {
        this.sendPaymentRemindersUseCase = sendPaymentRemindersUseCase;
    }

    @Scheduled(cron = "0 0 8 * * *", zone = "America/Bogota")
    public void sendPaymentReminders() {
        logger.info("Iniciando job de recordatorios de pago");
        try {
            sendPaymentRemindersUseCase.execute();
            logger.info("Job de recordatorios completado exitosamente");
        } catch (Exception e) {
            logger.error("Error en job de recordatorios: {}", e.getMessage(), e);
        }
    }
}