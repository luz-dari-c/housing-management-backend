package com.backend.housing.infrastructure.adapters.out;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.backend.housing.domain.entity.Role;

public interface SpringDataRoleRepository 
extends JpaRepository <Role, Long>{

    Optional<Role> findByName(String name);

    
}