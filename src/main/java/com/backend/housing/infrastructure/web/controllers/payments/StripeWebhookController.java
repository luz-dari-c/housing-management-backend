package com.backend.housing.infrastructure.web.controllers.payments;

import com.backend.housing.application.commands.payments.HandlePaymentWebhookCommand;
import com.backend.housing.domain.ports.in.payments.HandlePaymentWebhookUseCase;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class StripeWebhookController {

    private final HandlePaymentWebhookUseCase handlePaymentWebhookUseCase;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(HandlePaymentWebhookUseCase handlePaymentWebhookUseCase) {
        this.handlePaymentWebhookUseCase = handlePaymentWebhookUseCase;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {

                Session session = (Session) event.getData().getObject();

                if (session == null) {
                    return ResponseEntity.badRequest().body("Invalid session data");
                }

                String sessionId = session.getId();
                Long created = session.getCreated();

                String referenceId = session.getMetadata().get("referenceId");
                String referenceType = session.getMetadata().get("referenceType");

                if (referenceId == null || referenceType == null) {
                    return ResponseEntity.badRequest().body("Missing metadata in Stripe session");
                }

                LocalDate paymentConfirmedDate = Instant
                        .ofEpochSecond(created)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate();

                HandlePaymentWebhookCommand command = HandlePaymentWebhookCommand.builder()
                        .checkoutSessionId(sessionId)
                        .referenceId(UUID.fromString(referenceId))
                        .referenceType(referenceType)
                        .paymentConfirmedDate(paymentConfirmedDate)
                        .build();

                handlePaymentWebhookUseCase.execute(command);
            }

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing webhook: " + e.getMessage());
        }
    }
}