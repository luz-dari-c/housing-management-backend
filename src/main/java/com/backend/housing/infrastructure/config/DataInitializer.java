package com.backend.housing.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.ports.out.RoleRepositoryPort;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryPort roleRepository;

    public DataInitializer(RoleRepositoryPort roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String[] args) throws Exception {

        System.out.println("===== INITIALIZING ROLES =====");

        createRoleIfNotExist("ADMIN");
        createRoleIfNotExist("OWNER");
        createRoleIfNotExist("TENANT");
        createRoleIfNotExist("BUYER");

        System.out.println("===== ROLES INITIALIZED =====");
    }

    private void createRoleIfNotExist(String roleName) {

        roleRepository.findByName(roleName)
                .ifPresentOrElse(
                        role -> System.out.println("Role already exists: " + roleName),
                        () -> {
                            roleRepository.save(new Role(roleName));
                            System.out.println("Created role: " + roleName);
                        });
    }
}