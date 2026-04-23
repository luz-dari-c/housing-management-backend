package com.backend.housing.infrastructure.persistence.adapters.users;


import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.entity.users.UserBasicInfo;
import com.backend.housing.domain.entity.users.Rol;
import com.backend.housing.domain.ports.out.users.UserRoleServicePort;
import com.backend.housing.infrastructure.persistence.entities.users.UserEntity;
import com.backend.housing.infrastructure.persistence.entities.users.RolEntity;
import com.backend.housing.infrastructure.persistence.repositories.users.UserJpaRepository;
import com.backend.housing.infrastructure.persistence.repositories.users.RolJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRoleServiceAdapter implements UserRoleServicePort {

    private final UserJpaRepository userRepository;
    private final RolJpaRepository rolRepository;

    public UserRoleServiceAdapter(UserJpaRepository userRepository,
                                  RolJpaRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    private Optional<UserEntity> findUserEntityById(Long userId) {
        return userRepository.findById(userId);
    }

    private boolean hasRole(Long userId, String roleName) {
        return findUserEntityById(userId)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(roleName)))
                .orElse(false);
    }

    @Override
    public boolean isUserOwner(Long userId) {
        return hasRole(userId, "OWNER");
    }

    @Override
    public boolean isUserTenant(Long userId) {
        return hasRole(userId, "TENANT");
    }

    @Override
    public boolean isUserBuyer(Long userId) {
        return hasRole(userId, "BUYER");
    }



    private User assignRole(Long userId, String roleName) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        RolEntity roleEntity = rolRepository.findByName(roleName)
                .orElseGet(() -> {
                    RolEntity newRole = new RolEntity();
                    newRole.setName(roleName);
                    return rolRepository.save(newRole);
                });

        userEntity.getRoles().add(roleEntity);
        UserEntity savedUser = userRepository.save(userEntity);

        return toDomainUser(savedUser);
    }

    @Override
    public User assignOwnerRole(Long userId) {
        return assignRole(userId, "OWNER");
    }

    @Override
    public User assignTenantRole(Long userId) {
        return assignRole(userId, "TENANT");
    }

    @Override
    public User assignBuyerRole(Long userId) {
        return assignRole(userId, "BUYER");
    }

    @Override
    public User assignAdminRole(Long userId) {
        return assignRole(userId, "ADMIN");
    }

    private User removeRole(Long userId, String roleName) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userEntity.getRoles().removeIf(role -> role.getName().equals(roleName));
        UserEntity savedUser = userRepository.save(userEntity);

        return toDomainUser(savedUser);
    }

    @Override
    public User removeOwnerRole(Long userId) {
        return removeRole(userId, "OWNER");
    }

    @Override
    public User removeTenantRole(Long userId) {
        return removeRole(userId, "TENANT");
    }

    @Override
    public Optional<UserBasicInfo> getUserBasicInfo(Long userId) {
        return findUserEntityById(userId)
                .map(user -> new UserBasicInfo(
                        user.getPrimerNombre(),
                        user.getPrimerApellido(),
                        user.getEmail()
                ));
    }

    @Override
    public Optional<String> getUserEmail(Long userId) {
        return findUserEntityById(userId)
                .map(UserEntity::getEmail);
    }

    @Override
    public Optional<String> getUserName(Long userId) {
        return findUserEntityById(userId)
                .map(user -> {
                    String firstName = user.getPrimerNombre() != null ? user.getPrimerNombre() : "";
                    String lastName = user.getPrimerApellido() != null ? user.getPrimerApellido() : "";
                    return (firstName + " " + lastName).trim();
                });
    }

    @Override
    public Set<String> getUserRoles(Long userId) {
        return findUserEntityById(userId)
                .map(user -> user.getRoles().stream()
                        .map(RolEntity::getName)
                        .collect(Collectors.toSet()))
                .orElse(Set.of());
    }

    @Override
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    private User toDomainUser(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setPrimerNombre(entity.getPrimerNombre());
        user.setSegundoNombre(entity.getSegundoNombre());
        user.setPrimerApellido(entity.getPrimerApellido());
        user.setSegundoApellido(entity.getSegundoApellido());
        user.setEmail(entity.getEmail());
        user.setCedula(entity.getCedula());
        user.setPassword(entity.getPassword());
        user.setActive(entity.isActive());
        user.setEdad(entity.getEdad());

        Set<Rol> roles = entity.getRoles().stream()
                .map(roleEntity -> new Rol(roleEntity.getId(), roleEntity.getName()))
                .collect(Collectors.toSet());
        user.setRol(roles);

        return user;
    }
}
