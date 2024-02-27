package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NuevaVentaRequest {
    private Long clienteId;
    private List<DetalleVentaRequest> detalles;
}
