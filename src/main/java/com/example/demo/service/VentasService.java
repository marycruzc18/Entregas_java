package com.example.demo.service;

import com.example.demo.models.Ventas;
import com.example.demo.repository.VentasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentasService {
    @Autowired
    private VentasRepository ventasRepository;

    public List<Ventas> obtenerTodasVentas(){
        return ventasRepository.findAll();
    }

    public void guardarVenta(Ventas ventas){
        ventasRepository.save(ventas);
    }

    public void eliminarVenta(Long id) {
        Optional<Ventas> optionalVentas = ventasRepository.findById(id);
        if (optionalVentas.isPresent()) {
            ventasRepository.delete(optionalVentas.get());
        } else {
            throw new EntityNotFoundException("Venta no encontrada con el ID: " + id);
        }
    }
}
