package com.backend.housing.application.dto.request.chat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartChatRequest {
    @NotNull(message = "El ID del propietario es obligatorio")
    private Long ownerId;

    @NotNull(message = "El ID de la propiedad es obligatorio")
    private Long propertyId;
}