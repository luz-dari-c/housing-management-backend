
package com.backend.housing.application.services.auth;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.UserBasicInfo;
import com.backend.housing.domain.ports.out.RoleRepositoryPort;
import com.backend.housing.domain.ports.out.UserRepositoryPort;
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
    public Optional<UserBasicInfo> getUserBasicInfo(String userEmail) {
        return userRepositoryPort.findByEmail(userEmail)
                .map(user -> new UserBasicInfo(
                        user.getPrimerNombre(),
                        user.getPrimerApellido(),
                        user.getEmail()
                ));
    }
}