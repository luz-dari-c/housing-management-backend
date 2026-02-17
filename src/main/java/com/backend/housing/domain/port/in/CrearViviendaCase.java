package com.backend.housing.domain.port.in;

import com.backend.housing.domain.entity.TipoDeVivienda;
import com.backend.housing.domain.entity.Vivienda;

public interface CrearViviendaCase {


    Vivienda crearVivienda(
            String titulo, String direccion,
            double precioMensual, TipoDeVivienda tipo
    );

}
