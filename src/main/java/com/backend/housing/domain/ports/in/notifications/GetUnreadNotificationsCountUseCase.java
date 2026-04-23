package com.backend.housing.domain.ports.in.notifications;

public interface GetUnreadNotificationsCountUseCase {
    int execute(Long userId);
}