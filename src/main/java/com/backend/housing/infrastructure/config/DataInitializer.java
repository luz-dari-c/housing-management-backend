// RUTA: src/main/java/com/backend/housing/infrastructure/config/DataInitializer.java
package com.backend.housing.infrastructure.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.out.RoleRepositoryPort;
import com.backend.housing.domain.ports.out.UserRepositoryPort;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryPort roleRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepositoryPort roleRepositoryPort, 
                          UserRepositoryPort userRepositoryPort,
                          PasswordEncoder passwordEncoder) {
        this.roleRepositoryPort = roleRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("===== INITIALIZING ROLES =====");
        
      
        createRoleIfNotFound("USER");      // Rol por defecto para todos
        createRoleIfNotFound("OWNER");     // Para quienes publican
        createRoleIfNotFound("TENANT");    // Para quienes arriendan
        createRoleIfNotFound("BUYER");     // Para quienes compran
        
        System.out.println("===== ROLES INITIALIZED =====");
        
        
        createTestUserIfNotFound();
    }

    private void createRoleIfNotFound(String roleName) {
        roleRepositoryPort.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role(roleName);
                    Role saved = roleRepositoryPort.save(role);
                    System.out.println("Created role: " + roleName);
                    return saved;
                });
        System.out.println("Role already exists: " + roleName);
    }

    private void createTestUserIfNotFound() {
        String testEmail = "test@housing.com";
        if (userRepositoryPort.findByEmail(testEmail).isEmpty()) {
            Role userRole = roleRepositoryPort.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

            User testUser = new User();
            testUser.setPrimerNombre("Test");
            testUser.setPrimerApellido("User");
            testUser.setEmail(testEmail);
            testUser.setPassword(passwordEncoder.encode("Test123!"));
            testUser.setActive(true);
            testUser.setRoles(Set.of(userRole));

            userRepositoryPort.save(testUser);
            System.out.println("Usuario de prueba creado: " + testEmail + " / Test123!");
        }
    }
}