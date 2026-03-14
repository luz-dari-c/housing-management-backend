
package com.backend.housing.domain.ports.out.external;

public interface PaymentStatsPort {
    double getTotalAdminIncome();
    double getMonthlyAdminIncome();
    long getTotalPaymentsCount();
}