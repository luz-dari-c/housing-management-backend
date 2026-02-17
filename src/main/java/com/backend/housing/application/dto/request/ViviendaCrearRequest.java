package com.backend.housing.application.dto.request;


import com.backend.housing.domain.entity.TipoDeVivienda;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ViviendaCrearRequest {

    private String titulo;
    private String direccion;

    @JsonProperty("precioMensual")
    private double precioMensual;

    private TipoDeVivienda tipo;

    // 🔥 IMPORTANTE
    public ViviendaCrearRequest() {}

    // GETTERS

    public String getTitulo() { return titulo; }
    public String getDireccion() { return direccion; }
    public double getPrecioMensual() { return precioMensual; }
    public TipoDeVivienda getTipo() { return tipo; }
}