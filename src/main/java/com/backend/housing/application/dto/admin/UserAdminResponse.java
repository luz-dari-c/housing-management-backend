
package com.backend.housing.application.dto.admin;

import java.util.Set;
import java.util.stream.Collectors;

import com.backend.housing.domain.entity.User;

public class UserAdminResponse {
    private Long id;
    private String primerNombre;
    private String primerApellido;
    private String email;
    private String cedula;
    private boolean active;
    private Set<String> roles;

    public UserAdminResponse(User user) {
        this.id = user.getId();
        this.primerNombre = user.getPrimerNombre();
        this.primerApellido = user.getPrimerApellido();
        this.email = user.getEmail();
        this.cedula = user.getCedula();
        this.active = user.isActive();
        this.roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
    }


    public Long getId() { return id; }
    public String getPrimerNombre() { return primerNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public String getEmail() { return email; }
    public String getCedula() { return cedula; }
    public boolean isActive() { return active; }
    public Set<String> getRoles() { return roles; }
}