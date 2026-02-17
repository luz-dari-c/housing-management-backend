package com.backend.housing.infrastructure.adapters.out.persistence.jpa;

import com.backend.housing.domain.entity.Vivienda;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ViviendaRepository extends JpaRepository<Vivienda, Long> {
}
