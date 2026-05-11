package com.backend.housing.application.dto.request.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;
}