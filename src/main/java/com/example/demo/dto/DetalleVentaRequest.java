package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleVentaRequest {

    private Long productoId;
    private int cantidad;
    private double precioUnitario;
    private Long ventaId;
}
