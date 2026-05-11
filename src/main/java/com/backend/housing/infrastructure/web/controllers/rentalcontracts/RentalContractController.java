package com.backend.housing.infrastructure.web.controllers.rentalcontracts;

import com.backend.housing.application.commands.rentalcontracts.CancelContractCommand;
import com.backend.housing.application.commands.rentalcontracts.CreateContractCommand;
import com.backend.housing.application.commands.rentalcontracts.TerminateContractCommand;
import com.backend.housing.application.dto.request.rentalcontracts.CreateContractRequest;
import com.backend.housing.application.dto.response.rentalcontracts.ContractResponse;
import com.backend.housing.application.mapper.rentalcontracts.ContractMapper;
import com.backend.housing.domain.entity.properties.Property;
import com.backend.housing.domain.entity.rentalcontracts.RentalContract;
import com.backend.housing.domain.entity.rentalcontracts.valueobjects.ContractId;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.rentalcontracts.CancelContractUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.CreateContractUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.GetContractUseCase;
import com.backend.housing.domain.ports.in.rentalcontracts.TerminateContractUseCase;
import com.backend.housing.domain.ports.out.external.PropertyServicePort;
import com.backend.housing.domain.ports.out.properties.UserValidationPort;
import com.backend.housing.infrastructure.pdf.RentalContractPdfGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Rental Contracts", description = "Gestión de contratos de arriendo")
@RestController
@RequestMapping("/api/contracts")
public class RentalContractController {

    private final CreateContractUseCase createContractUseCase;
    private final TerminateContractUseCase terminateContractUseCase;
    private final GetContractUseCase getContractUseCase;
    private final ContractMapper contractMapper;
    private final PropertyServicePort propertyService;
    private final UserValidationPort userValidationPort;
    private final CancelContractUseCase cancelContractUseCase;
    private final RentalContractPdfGenerator rentalContractPdfGenerator;

    public RentalContractController(CreateContractUseCase createContractUseCase,
                                    TerminateContractUseCase terminateContractUseCase,
                                    GetContractUseCase getContractUseCase,
                                    ContractMapper contractMapper,
                                    PropertyServicePort propertyService,
                                    UserValidationPort userValidationPort,
                                    CancelContractUseCase cancelContractUseCase,
                                    RentalContractPdfGenerator rentalContractPdfGenerator) {
        this.createContractUseCase = createContractUseCase;
        this.terminateContractUseCase = terminateContractUseCase;
        this.getContractUseCase = getContractUseCase;
        this.contractMapper = contractMapper;
        this.propertyService = propertyService;
        this.userValidationPort = userValidationPort;
        this.cancelContractUseCase = cancelContractUseCase;
        this.rentalContractPdfGenerator = rentalContractPdfGenerator;
    }

    @Operation(summary = "Crear un nuevo contrato de arriendo")
    @PostMapping
    public ResponseEntity<ContractResponse> createContract(
            @Valid @RequestBody CreateContractRequest request) {

        User user = getAuthenticatedUser();

        CreateContractCommand command = contractMapper.toCommand(request, user.getId());
        RentalContract contract = createContractUseCase.createContract(command);

        Property property = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        String tenantName = userValidationPort.getUserName(contract.getTenantId())
                .orElse("Unknown");
        String ownerName = userValidationPort.getUserName(contract.getOwnerId())
                .orElse("Unknown");

        ContractResponse response = contractMapper.toResponse(contract, property, tenantName, ownerName);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener un contrato por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ContractResponse> getContract(@PathVariable UUID id) {

        User user = getAuthenticatedUser();

        RentalContract contract = getContractUseCase.getContract(ContractId.of(id), user.getId());

        Property property = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        String tenantName = userValidationPort.getUserName(contract.getTenantId())
                .orElse("Unknown");
        String ownerName = userValidationPort.getUserName(contract.getOwnerId())
                .orElse("Unknown");

        ContractResponse response = contractMapper.toResponse(contract, property, tenantName, ownerName);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Terminar un contrato activo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> terminateContract(@PathVariable UUID id) {

        User user = getAuthenticatedUser();

        TerminateContractCommand command = new TerminateContractCommand(ContractId.of(id), user.getId());
        terminateContractUseCase.terminateContract(command);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar contratos donde soy arrendatario (TENANT)")
    @GetMapping("/my/tenants")
    public ResponseEntity<List<ContractResponse>> getMyTenantContracts() {

        User user = getAuthenticatedUser();

        List<RentalContract> contracts = getContractUseCase.getContractsByTenant(user.getId());

        List<ContractResponse> responses = contracts.stream()
                .map(contract -> {
                    Property property = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                            .orElseThrow(() -> new RuntimeException("Property not found"));
                    String tenantName = userValidationPort.getUserName(contract.getTenantId())
                            .orElse("Unknown");
                    String ownerName = userValidationPort.getUserName(contract.getOwnerId())
                            .orElse("Unknown");
                    return contractMapper.toResponse(contract, property, tenantName, ownerName);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Listar contratos donde soy propietario (OWNER)")
    @GetMapping("/my/owners")
    public ResponseEntity<List<ContractResponse>> getMyOwnerContracts() {

        User user = getAuthenticatedUser();

        List<RentalContract> contracts = getContractUseCase.getContractsByOwner(user.getId());

        List<ContractResponse> responses = contracts.stream()
                .map(contract -> {
                    Property property = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                            .orElseThrow(() -> new RuntimeException("Property not found"));
                    String tenantName = userValidationPort.getUserName(contract.getTenantId())
                            .orElse("Unknown");
                    String ownerName = userValidationPort.getUserName(contract.getOwnerId())
                            .orElse("Unknown");
                    return contractMapper.toResponse(contract, property, tenantName, ownerName);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }


    @Operation(summary = "Cancelar un contrato de arriendo")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ContractResponse> cancelContract(@PathVariable UUID id) {

        User user = getAuthenticatedUser();

        CancelContractCommand command = new CancelContractCommand(ContractId.of(id), user.getId());
        RentalContract contract = cancelContractUseCase.execute(command);

        Property property = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        String tenantName = userValidationPort.getUserName(contract.getTenantId()).orElse("Unknown");
        String ownerName  = userValidationPort.getUserName(contract.getOwnerId()).orElse("Unknown");

        return ResponseEntity.ok(contractMapper.toResponse(contract, property, tenantName, ownerName));
    }

    @Operation(summary = "Descargar contrato en PDF")
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getContractPdf(@PathVariable UUID id) {
        User user = getAuthenticatedUser();

        RentalContract contract = getContractUseCase.getContract(ContractId.of(id), user.getId());

        Property property = propertyService.getPropertyBasicInfo(contract.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        String tenantName = userValidationPort.getUserName(contract.getTenantId()).orElse("—");
        String ownerName  = userValidationPort.getUserName(contract.getOwnerId()).orElse("—");

        ContractResponse response = contractMapper.toResponse(contract, property, tenantName, ownerName);
        byte[] pdf = rentalContractPdfGenerator.generate(response);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"contrato_" + id + ".pdf\"")
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