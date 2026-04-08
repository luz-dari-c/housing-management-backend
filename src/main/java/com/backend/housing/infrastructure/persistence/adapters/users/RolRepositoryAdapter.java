package com.backend.housing.infrastructure.persistence.adapters.users;

import com.backend.housing.domain.entity.users.Rol;
import com.backend.housing.domain.ports.out.users.RoleRepositoryPort;
import com.backend.housing.infrastructure.persistence.mappers.users.RolMapper;
import com.backend.housing.infrastructure.persistence.repositories.users.RolJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RolRepositoryAdapter implements RoleRepositoryPort {

    private final RolJpaRepository rolJpaRepository;

    public RolRepositoryAdapter(RolJpaRepository rolJpaRepository) {
        this.rolJpaRepository = rolJpaRepository;
    }

    @Override
    public Rol save(Rol role) {
        return RolMapper.toDomain(
                rolJpaRepository.save(RolMapper.toEntity(role))
        );
    }

    @Override
    public Optional<Rol> findByName(String name) {
        return rolJpaRepository.findByName(name)
                .map(RolMapper::toDomain);
    }
}
