
package com.backend.housing.application.services.auth;

import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.entity.properties.*;
import com.backend.housing.domain.ports.out.RoleRepositoryPort;
import com.backend.housing.domain.ports.out.UserRepositoryPort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.backend.housing.domain.ports.in.properties.UserBasicInfo;
import com.backend.housing.domain.ports.out.UserRoleServicePort;

@Service
@Transactional
public class UserRoleService implements UserRoleServicePort {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;

    public UserRoleService(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public boolean isUserOwner(String userEmail) {
        return userRepositoryPort.findByEmail(userEmail)
                .map(user -> user.getRoles().stream().anyMatch(role -> "OWNER".equals(role.getName())))
                .orElse(false);
    }

    @Override
    public User assignOwnerRole(String userEmail) {
        User user = userRepositoryPort.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + userEmail));

        
        boolean alreadyOwner = user.getRoles().stream().anyMatch(role -> "OWNER".equals(role.getName()));
        if (alreadyOwner) {
            return user; 
        }

        Role ownerRole = roleRepositoryPort.findByName("OWNER")
                .orElseThrow(() -> new RuntimeException("Error: Rol OWNER no encontrado en la base de datos."));

        
        Set<Role> updatedRoles = new HashSet<>(user.getRoles());
        updatedRoles.add(ownerRole);
        user.setRoles(updatedRoles);

        return userRepositoryPort.save(user);
    }

    @Override
    public boolean isUserTenant(String userEmail) {
        return userRepositoryPort.findByEmail(userEmail)
                .map(user -> user.getRoles().stream().anyMatch(role -> "TENANT".equals(role.getName())))
                .orElse(false);
    }
    
    @Override
    public User assignTenantRole(String userEmail) {
        User user = userRepositoryPort.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + userEmail));

        boolean alreadyTenant = user.getRoles().stream().anyMatch(role -> "TENANT".equals(role.getName()));
        if (alreadyTenant) {
            return user;
        }

        Role tenantRole = roleRepositoryPort.findByName("TENANT")
                .orElseThrow(() -> new RuntimeException("Error: Rol TENANT no encontrado"));

        Set<Role> updatedRoles = new HashSet<>(user.getRoles());
        updatedRoles.add(tenantRole);
        user.setRoles(updatedRoles);

        return userRepositoryPort.save(user);
    }

    @Override
    public User assignBuyerRole(String userEmail) {
        User user = userRepositoryPort.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + userEmail));

        boolean alreadyBuyer = user.getRoles().stream().anyMatch(role -> "BUYER".equals(role.getName()));
        if (alreadyBuyer) {
            return user;
        }

        Role buyerRole = roleRepositoryPort.findByName("BUYER")
                .orElseThrow(() -> new RuntimeException("Error: Rol BUYER no encontrado"));

        Set<Role> updatedRoles = new HashSet<>(user.getRoles());
        updatedRoles.add(buyerRole);
        user.setRoles(updatedRoles);

        return userRepositoryPort.save(user);
    }

    @Override
    public Optional<UserBasicInfo> getUserBasicInfo(String userEmail) {
        return userRepositoryPort.findByEmail(userEmail)
                .map(user -> new UserBasicInfo(
                        user.getPrimerNombre(),
                        user.getPrimerApellido(),
                        user.getEmail()
                ));
    }
    
    @Override
    public Set<String> getUserRoles(String userEmail) {
        return userRepositoryPort.findByEmail(userEmail)
                .map(user -> {
                    Set<String> roleNames = new HashSet<>();
                    for (Role role : user.getRoles()) {
                        roleNames.add(role.getName());
                    }
                    return roleNames;
                })
                .orElse(new HashSet<>());
    }
}