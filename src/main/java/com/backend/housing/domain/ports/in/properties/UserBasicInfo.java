
package com.backend.housing.domain.ports.in.properties;

public class UserBasicInfo {
    private String primerNombre;
    private String primerApellido;
    private String email;

    public UserBasicInfo(String primerNombre, String primerApellido, String email) {
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.email = email;
    }

    
    public String getPrimerNombre() { 
        return primerNombre; 
    }
    
    public String getPrimerApellido() { 
        return primerApellido; 
    }
    
    public String getEmail() { 
        return email; 
    }

    
    public void setPrimerNombre(String primerNombre) { 
        this.primerNombre = primerNombre; 
    }
    
    public void setPrimerApellido(String primerApellido) { 
        this.primerApellido = primerApellido; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getFullName() {
        return (primerNombre != null ? primerNombre : "") + " " + (primerApellido != null ? primerApellido : "");
    }
}