
package com.backend.housing.domain.ports.out.external;

import com.backend.housing.application.dto.admin.PaymentAdminResponse;
import java.util.List;

public interface PaymentAdminPort {
    List<PaymentAdminResponse> getAllPayments();
    double getTotalAdminIncome();
    double getMonthlyAdminIncome();
    long getTotalPaymentsCount();
}