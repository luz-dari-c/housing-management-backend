package com.backend.housing.infrastructure.adapters.out.persistence.jpa;


import com.backend.housing.domain.entity.Vivienda;
import com.backend.housing.domain.ports.out.ViviendaRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ViviendaJpaAdapter  implements ViviendaRepositoryPort {

    @Autowired
    private  ViviendaRepository viviendaRepository;

    @Override
    public Vivienda guardar(Vivienda vivienda){
        return viviendaRepository.save(vivienda);

    }

    @Override
    public Optional<Vivienda> buscarPorId(Long id){
        return viviendaRepository.findById(id);
    }


}
