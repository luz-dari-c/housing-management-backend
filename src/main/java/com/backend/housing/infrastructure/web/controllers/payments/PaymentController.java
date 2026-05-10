package com.backend.housing.infrastructure.web.controllers.payments;

import com.backend.housing.application.commands.payments.InitiatePaymentCommand;
import com.backend.housing.application.dto.response.payments.PaymentHistoryResponse;
import com.backend.housing.domain.entity.payments.valueobjects.PaymentId;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.payments.GetPaymentHistoryUseCase;
import com.backend.housing.domain.ports.in.payments.GetPaymentReceiptUseCase;
import com.backend.housing.domain.ports.in.payments.InitiatePaymentUseCase;
import com.backend.housing.domain.ports.in.payments.InitiatePeriodicPaymentUseCase;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Payments", description = "Gestión de pagos")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final InitiatePaymentUseCase initiatePaymentUseCase;
    private final InitiatePeriodicPaymentUseCase initiatePeriodicPaymentUseCase;
    private final GetPaymentHistoryUseCase getPaymentHistoryUseCase;
    private final UserValidationPort userValidationPort;
    private final GetPaymentReceiptUseCase getPaymentReceiptUseCase;

    public PaymentController(InitiatePaymentUseCase initiatePaymentUseCase,
                             InitiatePeriodicPaymentUseCase initiatePeriodicPaymentUseCase,
                             GetPaymentHistoryUseCase getPaymentHistoryUseCase,
                             UserValidationPort userValidationPort,
                             GetPaymentReceiptUseCase getPaymentReceiptUseCase) {
        this.initiatePaymentUseCase = initiatePaymentUseCase;
        this.initiatePeriodicPaymentUseCase = initiatePeriodicPaymentUseCase;
        this.getPaymentHistoryUseCase = getPaymentHistoryUseCase;
        this.userValidationPort = userValidationPort;
        this.getPaymentReceiptUseCase = getPaymentReceiptUseCase;
    }

    @Operation(summary = "Iniciar pago de un contrato (redirige a Stripe) - SOLO PRIMER PAGO")
    @PostMapping("/initiate/{contractId}")
    public ResponseEntity<Map<String, String>> initiatePayment(@PathVariable UUID contractId) {
        User currentUser = getAuthenticatedUser();

        InitiatePaymentCommand command = InitiatePaymentCommand.builder()
                .contractId(ContractId.of(contractId))
                .tenantId(currentUser.getId())
                .build();

        String checkoutUrl = initiatePaymentUseCase.initiatePayment(command);

        return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));
    }

    @Operation(summary = "Pagar período actual (mensual/semanal/quincenal) - PAGOS RECURRENTES")
    @PostMapping("/periodic/{contractId}")
    public ResponseEntity<Map<String, String>> payCurrentPeriod(@PathVariable UUID contractId) {
        User currentUser = getAuthenticatedUser();
        String checkoutUrl = initiatePeriodicPaymentUseCase.execute(
                ContractId.of(contractId),
                currentUser.getId()
        );
        return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));
    }

    @Operation(summary = "Ver historial de pagos de un contrato")
    @GetMapping("/contract/{contractId}/history")
    public ResponseEntity<PaymentHistoryResponse> getPaymentHistory(@PathVariable UUID contractId) {
        User currentUser = getAuthenticatedUser();
        PaymentHistoryResponse history = getPaymentHistoryUseCase.execute(
                ContractId.of(contractId),
                currentUser.getId()
        );
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Descargar comprobante del último pago del contrato")
    @GetMapping("/receipt/contract/{contractId}")
    public ResponseEntity<byte[]> getReceiptByContract(@PathVariable UUID contractId) {
        User currentUser = getAuthenticatedUser();
        byte[] pdf = getPaymentReceiptUseCase.executeByContractId(contractId, currentUser.getId());
        return buildPdfResponse(pdf, "comprobante_contrato_" + contractId + ".pdf");
    }

    @Operation(summary = "Descargar comprobante de un pago específico")
    @GetMapping("/receipt/payment/{paymentId}")
    public ResponseEntity<byte[]> getReceiptByPayment(@PathVariable UUID paymentId) {
        User currentUser = getAuthenticatedUser();
        byte[] pdf = getPaymentReceiptUseCase.executeByPaymentId(PaymentId.of(paymentId), currentUser.getId());
        return buildPdfResponse(pdf, "comprobante_pago_" + paymentId + ".pdf");
    }

    @Operation(summary = "Ver comprobante de pago de un contrato (legacy - último pago)")
    @GetMapping("/receipt/{contractId}")
    public ResponseEntity<byte[]> getPaymentReceiptLegacy(@PathVariable UUID contractId) {
        return getReceiptByContract(contractId);
    }

    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdf, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
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