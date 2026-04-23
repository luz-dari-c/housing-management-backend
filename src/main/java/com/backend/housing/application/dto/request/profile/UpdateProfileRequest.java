package com.backend.housing.application.dto.request.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    
    @Email(message = "Email inválido")
    private String email;
    
    @Positive(message = "La edad debe ser un número positivo")
    private Integer edad;
    
    @Size(min = 7, max = 10, message = "El número de teléfono debe tener entre 7 y 10 caracteres")
    private String phoneNumber;
    
    private String profilePictureUrl;
}