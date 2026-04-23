package com.backend.housing.infrastructure.persistence.mappers.users;

import java.util.Set;
import java.util.stream.Collectors;

import com.backend.housing.domain.entity.users.Rol;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.infrastructure.persistence.entities.users.RolEntity;
import com.backend.housing.infrastructure.persistence.entities.users.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setPrimerNombre(user.getPrimerNombre());
        entity.setSegundoNombre(user.getSegundoNombre());
        entity.setPrimerApellido(user.getPrimerApellido());
        entity.setSegundoApellido(user.getSegundoApellido());
        entity.setEmail(user.getEmail());
        entity.setCedula(user.getCedula());
        entity.setEdad(user.getEdad());
        entity.setPassword(user.getPassword());
        entity.setPhoneNumber(user.getPhoneNumber()); // NUEVO
        entity.setProfilePictureUrl(user.getProfilePictureUrl()); // NUEVO
        entity.setActive(user.isActive());

        if (user.getRoles() != null) {
            Set<RolEntity> roleEntities = user.getRoles().stream()
                    .map(role -> {
                        RolEntity rolEntity = new RolEntity();
                        rolEntity.setId(role.getId());
                        rolEntity.setName(role.getName());
                        return rolEntity;
                    })
                    .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }

        return entity;
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setId(entity.getId());
        user.setPrimerNombre(entity.getPrimerNombre());
        user.setSegundoNombre(entity.getSegundoNombre());
        user.setPrimerApellido(entity.getPrimerApellido());
        user.setSegundoApellido(entity.getSegundoApellido());
        user.setEmail(entity.getEmail());
        user.setCedula(entity.getCedula());
        user.setEdad(entity.getEdad());
        user.setPassword(entity.getPassword());
        user.setPhoneNumber(entity.getPhoneNumber()); // NUEVO
        user.setProfilePictureUrl(entity.getProfilePictureUrl()); // NUEVO
        user.setActive(entity.isActive());

        if (entity.getRoles() != null) {
            Set<Rol> roles = entity.getRoles().stream()
                    .map(roleEntity -> new Rol(roleEntity.getId(), roleEntity.getName()))
                    .collect(Collectors.toSet());
            user.setRol(roles);
        }

        return user;
    }
}