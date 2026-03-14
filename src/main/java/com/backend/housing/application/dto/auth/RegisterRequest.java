package com.backend.housing.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    
    @NotBlank(message = "El primer nombre es obligatorio")
    private String primerNombre;

    @NotBlank(message = "El segundo nombre es obligatorio") 
    private String segundoNombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    private String primerApellido;

    @NotBlank(message = "El segundo apellido es obligatorio")
    private String segundoApellido;

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 5, max = 20, message = "La cédula debe tener entre 5 y 20 caracteres")
    private String cedula;

    @NotNull(message = "La edad es obligatoria")
    @Positive(message = "La edad debe ser un número positivo")
    private Integer edad;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

   
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}