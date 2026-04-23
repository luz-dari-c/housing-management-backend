package com.backend.housing.infrastructure.persistence.mappers.users;
import com.backend.housing.domain.entity.users.Rol;
import com.backend.housing.infrastructure.persistence.entities.users.RolEntity;

public class RolMapper {

    public static Rol toDomain(RolEntity entity) {
        if (entity == null) return null;

        Rol role = new Rol();
        role.setId(entity.getId());
        role.setName(entity.getName());
        return role;
    }

    public static RolEntity toEntity(Rol domain) {
        if (domain == null) return null;

        RolEntity entity = new RolEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }
}