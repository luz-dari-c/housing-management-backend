package com.backend.housing.application.usecases;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.housing.domain.entity.TipoDeVivienda;
import com.backend.housing.domain.entity.Vivienda;
import com.backend.housing.domain.ports.out.ViviendaRepositoryPort;

@Service
public class CrearViviendaService implements CrearViviendaCase {

    @Autowired
    private ViviendaRepositoryPort viviendaRepository;

    @Override
    public Vivienda crearVivienda(String titulo, String direccion, double precioMensual, TipoDeVivienda tipo) {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vivienda no puede ser nulo.");
        }

        if (precioMensual <= 0) {
            throw new IllegalArgumentException("El precio mensual debe ser mayor que cero.");
        }

        Vivienda nuevaVivienda = new Vivienda(
                null,
                titulo,
                direccion,
                BigDecimal.valueOf(precioMensual),
                true,
                tipo
        );
        return viviendaRepository.guardar(nuevaVivienda);
    }

}
