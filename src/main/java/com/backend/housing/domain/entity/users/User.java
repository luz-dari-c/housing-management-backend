package com.backend.housing.domain.entity.users;

import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class User {

    private Long id;

    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;

    private String email;
    private String cedula;
    private String password;

    private boolean active;
    private int edad;

    private Set<Rol> roles = new HashSet<>();

    public User() {
    }

    public User(Long id, String primerNombre, String primerApellido, String email) {
        this.id = id;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.email = email;
    }

    public void activar() {
        this.active = true;
    }

    public void desactivar() {
        this.active = false;
    }

    public void agregarRol(Rol role) {
        this.roles.add(role);
    }

    public void removerRol(Rol role) {
        this.roles.remove(role);
    }

    public void setName(String fullName) {
        if (fullName == null || fullName.isBlank()) return;

        String[] parts = fullName.trim().split(" ");
        this.primerNombre = parts[0];

        if (parts.length > 1) {
            this.primerApellido = parts[parts.length - 1];
        }
    }

    public Long getId() {
        return id;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getEmail() {
        return email;
    }

    public String getCedula() {
        return cedula;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public int getEdad() {
        return edad;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public void setSegundoNombre(@NotBlank(message = "El segundo nombre es obligatorio") String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public void setSegundoApellido(@NotBlank(message = "El segundo apellido es obligatorio") String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setRol(Set<Rol> roles) {
        this.roles = roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}