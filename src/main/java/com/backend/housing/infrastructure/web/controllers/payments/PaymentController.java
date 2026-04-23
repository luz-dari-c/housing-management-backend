package com.backend.housing.infrastructure.web.controllers.payments;

import com.backend.housing.application.commands.payments.InitiatePaymentCommand;
import com.backend.housing.application.dto.response.payments.PaymentReceiptResponse;
import com.backend.housing.application.dto.response.payments.PaymentResponse;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.payments.GetPaymentReceiptUseCase;
import com.backend.housing.domain.ports.in.payments.InitiatePaymentUseCase;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final UserValidationPort userValidationPort;
    private final GetPaymentReceiptUseCase getPaymentReceiptUseCase;


    public PaymentController(InitiatePaymentUseCase initiatePaymentUseCase,
                             UserValidationPort userValidationPort, GetPaymentReceiptUseCase getPaymentReceiptUseCase) {
        this.initiatePaymentUseCase = initiatePaymentUseCase;
        this.userValidationPort = userValidationPort;
        this.getPaymentReceiptUseCase = getPaymentReceiptUseCase;
    }

    @Operation(summary = "Iniciar pago de un contrato (redirige a Stripe)")
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

    @Operation(summary = "Ver comprobante de pago de un contrato")
    @GetMapping("/receipt/{contractId}")
    public ResponseEntity<PaymentReceiptResponse> getPaymentReceipt(@PathVariable UUID  contractId){
        User currentUser = getAuthenticatedUser();

        PaymentReceiptResponse receipt = getPaymentReceiptUseCase.execute(contractId, currentUser.getId());
        return ResponseEntity.ok(receipt);

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