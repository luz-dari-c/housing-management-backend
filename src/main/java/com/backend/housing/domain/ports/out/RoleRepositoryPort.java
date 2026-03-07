package com.backend.housing.domain.ports.out;

import com.backend.housing.domain.entity.Role;
import java.util.Optional;

public interface RoleRepositoryPort {

    Role save(Role role);

    Optional<Role> findByName(String name);

}