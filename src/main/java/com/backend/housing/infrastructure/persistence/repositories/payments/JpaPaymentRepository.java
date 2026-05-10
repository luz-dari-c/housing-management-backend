package com.backend.housing.infrastructure.persistence.repositories.payments;

import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.enums.PaymentStatus;
import com.backend.housing.infrastructure.persistence.entities.payments.PaymentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, UUID> {

    List<PaymentEntity> findByReferenceId(UUID referenceId, Pageable pageable);

    Optional<PaymentEntity> findByCheckoutSessionId(String checkoutSessionId);

    boolean existsByReferenceIdAndPeriod(UUID referenceId, String period);

    List<PaymentEntity> findByReferenceIdOrderByCreatedAtDesc(UUID referenceId);

    Optional<PaymentEntity> findFirstByReferenceIdAndStatusOrderByCreatedAtDesc(UUID referenceId, PaymentStatus status);
}