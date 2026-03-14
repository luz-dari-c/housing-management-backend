package com.backend.housing.infrastructure.adapters.out;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.backend.housing.domain.entity.Role;
import com.backend.housing.domain.ports.out.RoleRepositoryPort;


@Repository
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final SpringDataRoleRepository springDataRoleRepository;

    public RoleRepositoryAdapter(SpringDataRoleRepository springDataRoleRepository) {
        this.springDataRoleRepository = springDataRoleRepository;
    }

    @Override
    public Role save(Role role) {
        return springDataRoleRepository.save(role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return springDataRoleRepository.findByName(name);
    }
}