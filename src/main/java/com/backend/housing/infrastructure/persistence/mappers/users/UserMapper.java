package com.backend.housing.infrastructure.persistence.mappers.users;

import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.entity.users.Rol;
import com.backend.housing.infrastructure.persistence.entities.users.UserEntity;
import com.backend.housing.infrastructure.persistence.entities.users.RolEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;

        User user = new User();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setCedula(entity.getCedula());
        user.setPassword(entity.getPassword());
        user.setEdad(entity.getEdad());

        user.setName(entity.getPrimerNombre() + " " + entity.getPrimerApellido());

        // Roles
        Set<Rol> roles = entity.getRoles()
                .stream()
                .map(RolMapper::toDomain)
                .collect(Collectors.toSet());

        user.getRoles().addAll(roles);

        if (entity.isActive()) {
            user.activar();
        } else {
            user.desactivar();
        }

        return user;
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setCedula(domain.getCedula());
        entity.setPassword(domain.getPassword());
        entity.setEdad(domain.getEdad());

        entity.setPrimerNombre(domain.getPrimerNombre());
        entity.setSegundoNombre(domain.getSegundoNombre());
        entity.setPrimerApellido(domain.getPrimerApellido());
        entity.setSegundoApellido(domain.getSegundoApellido());

        entity.setActive(domain.isActive());

        // Roles
        Set<RolEntity> roles = domain.getRoles()
                .stream()
                .map(RolMapper::toEntity)
                .collect(Collectors.toSet());

        entity.setRoles(roles);

        return entity;
    }
}