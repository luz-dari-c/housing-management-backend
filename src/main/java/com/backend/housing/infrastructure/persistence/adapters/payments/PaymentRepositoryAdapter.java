package com.backend.housing.infrastructure.persistence.adapters.payments;

import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;
import com.backend.housing.domain.ports.out.payments.PaymentRepository;
import com.backend.housing.domain.valueobjects.Pagination;
import com.backend.housing.infrastructure.persistence.entities.payments.PaymentEntity;
import com.backend.housing.infrastructure.persistence.mappers.payments.PaymentEntityMapper;
import com.backend.housing.infrastructure.persistence.repositories.payments.JpaPaymentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final PaymentEntityMapper mapper;

    public PaymentRepositoryAdapter(JpaPaymentRepository jpaPaymentRepository,
                                    PaymentEntityMapper mapper) {
        this.jpaPaymentRepository = jpaPaymentRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {

        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }

        PaymentEntity entity = mapper.toEntity(payment);

        PaymentEntity savedEntity = jpaPaymentRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(PaymentId id) {

        if (id == null) {
            return Optional.empty();
        }

        Optional<PaymentEntity> optionalEntity =
                jpaPaymentRepository.findById(id.getValue());

        return optionalEntity.map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByReferenceId(UUID referenceId, Pagination pagination) {

        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getSize()
        );

        return jpaPaymentRepository
                .findByReferenceId(referenceId, pageable)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByCheckoutSessionId(String checkoutSessionId) {

        if (checkoutSessionId == null || checkoutSessionId.isBlank()) {
            return Optional.empty();
        }

        Optional<PaymentEntity> optionalEntity =
                jpaPaymentRepository.findByCheckoutSessionId(checkoutSessionId);

        return optionalEntity.map(mapper::toDomain);
    }
}