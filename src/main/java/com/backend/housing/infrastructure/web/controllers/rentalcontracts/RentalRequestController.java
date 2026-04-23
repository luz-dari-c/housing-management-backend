package com.backend.housing.infrastructure.web.controllers.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CreateRentalRequestCommand;
import com.backend.housing.application.dto.request.rentalcontracts.CreateRentalRequestRequest;
import com.backend.housing.application.dto.response.rentalcontracts.AcceptRequestResponse;
import com.backend.housing.application.dto.response.rentalcontracts.RentalRequestResponse;
import com.backend.housing.application.mapper.rentalcontracts.RentalRequestMapper;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.RentalRequest;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.rentalcontracts.AcceptRentalRequestUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.CreateRentalRequestUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.GetRentalRequestUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.RejectRentalRequestUseCase;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Rental Requests", description = "Gestión de solicitudes de arriendo")
@RestController
@RequestMapping("/api/rental-requests")
public class RentalRequestController {

    private final CreateRentalRequestUseCase createRentalRequestUseCase;
    private final GetRentalRequestUseCase getRentalRequestUseCase;
    private final RentalRequestMapper rentalRequestMapper;
    private final AcceptRentalRequestUseCase acceptRentalRequestUseCase;
    private final RejectRentalRequestUseCase rejectRentalRequestUseCase;
    private final UserValidationPort userValidationPort;

    public RentalRequestController(CreateRentalRequestUseCase createRentalRequestUseCase,
                                   GetRentalRequestUseCase getRentalRequestUseCase,
                                   RentalRequestMapper rentalRequestMapper,
                                   AcceptRentalRequestUseCase acceptRentalRequestUseCase,
                                   RejectRentalRequestUseCase rejectRentalRequestUseCase,
                                   UserValidationPort userValidationPort) {
        this.createRentalRequestUseCase = createRentalRequestUseCase;
        this.getRentalRequestUseCase = getRentalRequestUseCase;
        this.rentalRequestMapper = rentalRequestMapper;
        this.acceptRentalRequestUseCase = acceptRentalRequestUseCase;
        this.rejectRentalRequestUseCase = rejectRentalRequestUseCase;
        this.userValidationPort = userValidationPort;
    }

    @Operation(summary = "Crear nueva solicitud de arriendo")
    @PostMapping
    public ResponseEntity<RentalRequestResponse> createRentalRequest(
            @Valid @RequestBody CreateRentalRequestRequest request) {

        User user = getAuthenticatedUser();

        CreateRentalRequestCommand command = rentalRequestMapper.toCommand(request, user.getId());
        RentalRequest rentalRequest = createRentalRequestUseCase.create(command);

        RentalRequestResponse response = rentalRequestMapper.toResponse(rentalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar solicitudes de un propietario")
    @GetMapping("/owner")
    public ResponseEntity<List<RentalRequestResponse>> getRequestsByOwner() {
        User user = getAuthenticatedUser();
        List<RentalRequest> requests = getRentalRequestUseCase.getRequestsByOwner(user.getId());

        List<RentalRequestResponse> responses = requests.stream()
                .map(rentalRequestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Listar solicitudes de un arrendatario")
    @GetMapping("/tenant")
    public ResponseEntity<List<RentalRequestResponse>> getRequestsByTenant() {
        User user = getAuthenticatedUser();
        List<RentalRequest> requests = getRentalRequestUseCase.getRequestsByTenant(user.getId());

        List<RentalRequestResponse> responses = requests.stream()
                .map(rentalRequestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Aceptar una solicitud")
    @PostMapping("/{id}/accept")
    public ResponseEntity<AcceptRequestResponse> acceptRequest(@PathVariable String id) {
        User user = getAuthenticatedUser();
        RentalContract contract = acceptRentalRequestUseCase.execute(
                rentalRequestMapper.toRequestId(id),
                user.getId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AcceptRequestResponse(contract.getId().getValue()));
    }

    @Operation(summary = "Rechazar una solicitud")
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable String id) {
        User user = getAuthenticatedUser();
        rejectRentalRequestUseCase.execute(rentalRequestMapper.toRequestId(id), user.getId());
        return ResponseEntity.noContent().build();
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