package com.backend.housing.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "viviendas")
@Getter
@Setter
public class Vivienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean disponible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeVivienda tipo;

    public Vivienda() {
    }

    public Vivienda(Long id, String titulo, String direccion, BigDecimal precioMensual, boolean disponible, TipoDeVivienda tipo) {
        this.id = id;
        this.titulo = titulo;
        this.direccion = direccion;
        this.precio = precioMensual;
        this.disponible = disponible;
        this.tipo = tipo;
    }

    public boolean puedaAlquilarse() {
        return disponible;
    }

    public void marcarComoAlquilada() {
        if (!disponible) {
            throw new IllegalStateException("La vivienda no está disponible para alquilar.");
        }
        this.disponible = false;
    }


}
