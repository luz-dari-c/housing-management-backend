package com.backend.housing.infrastructure.adapters.in.web;


import com.backend.housing.application.dto.request.ViviendaCrearRequest;
import com.backend.housing.domain.entity.Vivienda;
import com.backend.housing.domain.port.in.CrearViviendaCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/viviendas")
public class ViviendaController {

    @Autowired
    private CrearViviendaCase crearViviendaCase;


    @PostMapping
    public ResponseEntity<Vivienda> crearVivienda(@RequestBody ViviendaCrearRequest request) {
        // ← Agrega esto
        System.out.println("=== DEBUG - Request recibida ===");
        System.out.println("Titulo: " + request.getTitulo());
        System.out.println("Direccion: " + request.getDireccion());
        System.out.println("Precio mensual: " + request.getPrecioMensual());
        System.out.println("Tipo: " + request.getTipo());
        if (request.getDireccion() == null) {
            System.out.println("¡ATENCIÓN! direccion es NULL en el DTO");
        }
        // ← fin debug

        Vivienda viviendaCreada = crearViviendaCase.crearVivienda(
                request.getTitulo(),
                request.getDireccion(),
                request.getPrecioMensual(),
                request.getTipo()
        );

        return ResponseEntity.ok(viviendaCreada);
    }
}
