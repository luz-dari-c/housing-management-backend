package com.backend.housing.domain.ports.out;

import com.backend.housing.domain.entity.Vivienda;

import java.util.Optional;


public interface ViviendaRepositoryPort {

    Vivienda guardar(Vivienda vivienda);
    Optional<Vivienda> buscarPorId(Long id);
}
