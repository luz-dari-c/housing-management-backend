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
    public void run(String [] args){
        createRoleIfNoExist("ADMIN");
        createRoleIfNoExist("OWNER");
        createRoleIfNoExist("TENANT");
        createRoleIfNoExist("BUYER");
    }

    private void createRoleIfNoExist(String roleName){
        roleRepository.findByName(roleName)
        .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

}
