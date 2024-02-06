package com.example.demo.controllers;


import com.example.demo.models.Ventas;
import com.example.demo.repository.VentasRepository;
import com.example.demo.service.VentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentasController {
    @Autowired
    private VentasService ventasService;

    // Obtener ventas
    @GetMapping("ventas")
    public ResponseEntity<List<Ventas>> getVentas() {
        List<Ventas> ventas = ventasService.obtenerTodasVentas();
        return ResponseEntity.ok(ventas);
    }

    //Guardar ventas en la base de datos
    @PostMapping("altaventas")
    public ResponseEntity<String> guardarVenta(@RequestBody Ventas venta) {
        try {
            // No necesitas analizar la fecha si ya está en formato LocalDateTime
            ventasService.guardarVenta(venta);
            return ResponseEntity.status(HttpStatus.CREATED).body("Venta guardada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la venta: " + e.getMessage());
        }
    }

   //Eliminar venta
    @DeleteMapping("bajaventa/{id}")
    public ResponseEntity<String> eliminarVenta(@PathVariable Long id) {
        try {
            ventasService.eliminarVenta(id);
            return ResponseEntity.ok("Venta eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo eliminar la venta: " + e.getMessage());
        }
    }
}
