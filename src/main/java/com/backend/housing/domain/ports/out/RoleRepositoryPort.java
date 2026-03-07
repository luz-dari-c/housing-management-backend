package com.backend.housing.domain.ports.out;

import java.util.Optional;

import com.backend.housing.domain.entity.Role;

public interface RoleRepositoryPort {

    Role save(Role role);

    Optional<Role> findByName(String name);
}