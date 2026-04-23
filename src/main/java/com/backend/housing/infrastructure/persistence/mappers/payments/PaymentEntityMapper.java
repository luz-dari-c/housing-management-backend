package com.backend.housing.infrastructure.persistence.mappers.payments;

import com.backend.housing.domain.entity.payments.Payment;
import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;
import com.backend.housing.infrastructure.persistence.entities.payments.PaymentEntity;
import org.springframework.stereotype.Component;


@Component
public class PaymentEntityMapper {

    public PaymentEntity toEntity(Payment domain){
        return new PaymentEntity(
                domain.getId() != null ? domain.getId().getValue() : null,
                domain.getReferenceId(),
                domain.getReferenceType(),
                domain.getAmount(),
                domain.getCurrency(),
                domain.getStatus(),
                domain.getMethod(),
                domain.getCheckoutSessionId(),
                domain.getCheckoutUrl(),
                domain.getCreatedAt(),
                domain.getPaidAt()
        );
    }

    public Payment toDomain(PaymentEntity entity){

        PaymentId paymentId = PaymentId.of(entity.getId());

        return Payment.reconstitute(
                paymentId,
                entity.getReferenceId(),
                entity.getReferenceType(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getMethod(),
                entity.getCreatedAt(),
                entity.getPaidAt(),
                entity.getCheckoutSessionId(),
                entity.getCheckoutUrl()
        );
    }
}