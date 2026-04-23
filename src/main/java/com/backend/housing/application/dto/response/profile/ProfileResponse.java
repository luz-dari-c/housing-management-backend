package com.backend.housing.application.dto.response.profile;

import java.util.Set;
import java.util.stream.Collectors;

import com.backend.housing.domain.entity.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private String cedula;
    private int edad;
    private String phoneNumber; // NUEVO
    private String profilePictureUrl; // NUEVO
    private boolean active;
    private Set<String> roles;

    public ProfileResponse(User user) {
        this.id = user.getId();
        this.primerNombre = user.getPrimerNombre();
        this.segundoNombre = user.getSegundoNombre();
        this.primerApellido = user.getPrimerApellido();
        this.segundoApellido = user.getSegundoApellido();
        this.email = user.getEmail();
        this.cedula = user.getCedula();
        this.edad = user.getEdad();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePictureUrl = user.getProfilePictureUrl();
        this.active = user.isActive();
        this.roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
    }
}
//si lees esto, estás actualizado 12*04*2026