package com.backend.housing.infrastructure.persistence.adapters.notifications;

import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.valueobjects.NotificationId;
import com.backend.housing.domain.ports.out.notifications.NotificationRepository;
import com.backend.housing.infrastructure.persistence.entities.notifications.NotificationEntity;
import com.backend.housing.infrastructure.persistence.mappers.notifications.NotificationEntityMapper;
import com.backend.housing.infrastructure.persistence.repositories.notifications.JpaNotificationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final JpaNotificationRepository jpaRepository;
    private final NotificationEntityMapper mapper;

    public NotificationRepositoryAdapter(JpaNotificationRepository jpaRepository,
                                         NotificationEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> findById(NotificationId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findByUserId(Long userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findUnreadByUserId(Long userId) {
        return jpaRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int countUnreadByUserId(Long userId) {
        return jpaRepository.countUnreadByUserId(userId);
    }

    @Override
    @Transactional
    public void markAsRead(NotificationId id) {
        jpaRepository.markAsRead(id.getValue());
    }
}