
package com.backend.housing.application.dto.auth.profile;

import java.util.Set;
import java.util.stream.Collectors;

import com.backend.housing.domain.entity.User;

public class ProfileResponse {
    private Long id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private String cedula;
    private int edad;
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
        this.active = user.isActive();
        this.roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
    }

    
    public Long getId() { return id; }
    public String getPrimerNombre() { return primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public String getEmail() { return email; }
    public String getCedula() { return cedula; }
    public int getEdad() { return edad; }
    public boolean isActive() { return active; }
    public Set<String> getRoles() { return roles; }
}