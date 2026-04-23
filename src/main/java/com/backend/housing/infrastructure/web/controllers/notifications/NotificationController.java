package com.backend.housing.infrastructure.web.controllers.notifications;

import com.backend.housing.application.dto.response.notifications.NotificationResponse;
import com.backend.housing.domain.entity.notifications.Notification;
import com.backend.housing.domain.entity.notifications.valueobjects.NotificationId;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.jobs.ExpireOverdueContractsUseCase;
import com.backend.housing.domain.ports.in.jobs.SendPaymentRemindersUseCase;
import com.backend.housing.domain.ports.in.notifications.GetUnreadNotificationsCountUseCase;
import com.backend.housing.domain.ports.in.notifications.GetUserNotificationsUseCase;
import com.backend.housing.domain.ports.in.notifications.MarkNotificationAsReadUseCase;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Notifications", description = "Gestión de notificaciones del usuario")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final GetUserNotificationsUseCase getUserNotificationsUseCase;
    private final GetUnreadNotificationsCountUseCase getUnreadNotificationsCountUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
    private final UserValidationPort userValidationPort;
    private final SendPaymentRemindersUseCase sendPaymentRemindersUseCase;
    private final ExpireOverdueContractsUseCase expireOverdueContractsUseCase;



    public NotificationController(GetUserNotificationsUseCase getUserNotificationsUseCase,
                                  GetUnreadNotificationsCountUseCase getUnreadNotificationsCountUseCase,
                                  MarkNotificationAsReadUseCase markNotificationAsReadUseCase,
                                  UserValidationPort userValidationPort, SendPaymentRemindersUseCase sendPaymentRemindersUseCase,
                                  ExpireOverdueContractsUseCase expireOverdueContractsUseCase
                                  ) {
        this.getUserNotificationsUseCase = getUserNotificationsUseCase;
        this.getUnreadNotificationsCountUseCase = getUnreadNotificationsCountUseCase;
        this.markNotificationAsReadUseCase = markNotificationAsReadUseCase;
        this.userValidationPort = userValidationPort;
        this.sendPaymentRemindersUseCase = sendPaymentRemindersUseCase;
        this.expireOverdueContractsUseCase = expireOverdueContractsUseCase;
    }

    @Operation(summary = "Obtener todas las notificaciones del usuario autenticado")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        User currentUser = getAuthenticatedUser();
        List<Notification> notifications = getUserNotificationsUseCase.execute(currentUser.getId());
        List<NotificationResponse> responses = notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtener notificaciones no leídas del usuario autenticado")
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        User currentUser = getAuthenticatedUser();
        List<Notification> notifications = getUserNotificationsUseCase.execute(currentUser.getId());
        List<NotificationResponse> unreadResponses = notifications.stream()
                .filter(n -> !n.isRead())
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(unreadResponses);
    }

    @Operation(summary = "Contar notificaciones no leídas")
    @GetMapping("/unread/count")
    public ResponseEntity<Integer> countUnreadNotifications() {
        User currentUser = getAuthenticatedUser();
        int count = getUnreadNotificationsCountUseCase.execute(currentUser.getId());
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Marcar una notificación como leída")
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        User currentUser = getAuthenticatedUser();
        markNotificationAsReadUseCase.execute(NotificationId.of(id), currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test/expire")
    public ResponseEntity<String> testExpireJob() {
        expireOverdueContractsUseCase.execute();
        return ResponseEntity.ok("Job de expiración ejecutado manualmente");
    }

    @PostMapping("/test/reminder")
    public ResponseEntity<String> testReminderJob() {
        sendPaymentRemindersUseCase.execute();
        return ResponseEntity.ok("Job de recordatorios ejecutado manualmente");
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId().getValue(),
                notification.getType().name(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead(),
                notification.getContractId() != null ? notification.getContractId().getValue() : null,
                notification.getCreatedAt()
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }
        String email = authentication.getName();
        return userValidationPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


}