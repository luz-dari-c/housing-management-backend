package com.backend.housing.domain.ports.out.users;


import com.backend.housing.domain.entity.users.Rol;

import java.util.Optional;


public interface RoleRepositoryPort {

    Rol save(Rol role);

    Optional<Rol> findByName(String name);
}
