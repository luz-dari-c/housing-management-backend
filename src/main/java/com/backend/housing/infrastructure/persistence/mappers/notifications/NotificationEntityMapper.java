package com.backend.housing.infrastructure.persistence.mappers.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.enums.NotificationType;
import com.backend.housing.domain.entity.notifications.valueobjects.NotificationId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.infrastructure.persistence.entities.notifications.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationEntityMapper {

    public NotificationEntity toEntity(Notification domain) {
        return new NotificationEntity(
                domain.getUserId(),
                domain.getType().name(),
                domain.getTitle(),
                domain.getMessage(),
                domain.isRead(),
                domain.getContractId() != null ? domain.getContractId().getValue() : null,
                domain.getCreatedAt()
        );
    }

    public Notification toDomain(NotificationEntity entity) {
        return Notification.reconstitute(
                NotificationId.of(entity.getId()),
                entity.getUserId(),
                NotificationType.valueOf(entity.getType()),
                entity.getTitle(),
                entity.getMessage(),
                entity.isRead(),
                entity.getContractId() != null ? ContractId.of(entity.getContractId()) : null,
                entity.getCreatedAt()
        );
    }
}