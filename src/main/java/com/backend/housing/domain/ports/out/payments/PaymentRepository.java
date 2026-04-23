package com.backend.housing.domain.ports.out.payments;

import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;
import com.backend.housing.domain.valueobjects.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(PaymentId id);

    List<Payment> findByReferenceId(UUID referenceId, Pagination pagination);

    Optional<Payment> findByCheckoutSessionId(String checkoutSessionId);
}