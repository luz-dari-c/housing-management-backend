package com.backend.housing.application.services.jobs;

import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.ports.in.jobs.SendPaymentRemindersUseCase;
import com.backend.housing.domain.ports.in.notifications.NotifyPaymentReminderUseCase;
import com.backend.housing.domain.ports.out.rentalcontracts.RentalContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SendPaymentRemindersService implements SendPaymentRemindersUseCase {

    private final RentalContractRepository contractRepository;
    private final NotifyPaymentReminderUseCase notifyPaymentReminderUseCase;

    public SendPaymentRemindersService(RentalContractRepository contractRepository,
                                       NotifyPaymentReminderUseCase notifyPaymentReminderUseCase) {
        this.contractRepository = contractRepository;
        this.notifyPaymentReminderUseCase = notifyPaymentReminderUseCase;
    }

    @Override
    @Transactional
    public void execute() {
        LocalDate today = LocalDate.now();
        LocalDate reminderThreshold = today.plusDays(3);

        List<RentalContract> activeContracts = contractRepository.findActiveContracts();

        for (RentalContract contract : activeContracts) {
            LocalDate paymentDueDate = contract.getPaymentDueDate();

            if (paymentDueDate != null && paymentDueDate.equals(reminderThreshold)) {
                sendReminder(contract);
            }
        }
    }

    private void sendReminder(RentalContract contract) {
        Long tenantId = contract.getTenantId();
        int daysRemaining = 3;

        notifyPaymentReminderUseCase.execute(tenantId, contract.getId(), daysRemaining);
    }
}