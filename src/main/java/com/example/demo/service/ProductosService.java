package com.example.demo.service;

import com.example.demo.models.Cliente;
import com.example.demo.models.Productos;
import com.example.demo.repository.ProductosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductosService {
    @Autowired
    private ProductosRepository productosRepository;

    public List<Productos> obtenerTodosProductos(){
        return productosRepository.findAll();
    }

    public Optional<Productos> buscarProductosPorId(Long id) {
        return productosRepository.findById(id); //
    }

    public void guardarProducto(Productos productos) {
        productosRepository.save(productos);
    }
    public void modificarProducto(Long id, Productos productos) {
        Optional<Productos> optionalProductos = productosRepository.findById(id);
        if (optionalProductos.isPresent()) {
            Productos updateProductos = optionalProductos.get();
            updateProductos.setDescripcion(productos.getDescripcion());
            updateProductos.setCodigo(productos.getCodigo());
            updateProductos.setStock(productos.getStock());
            updateProductos.setPrecio(productos.getPrecio());
            productosRepository.save(updateProductos);
        } else {
            throw new EntityNotFoundException("Producto no encontrado con el ID: " + id);
        }
    }

    public void eliminarProducto(Long id) {
        Optional<Productos> optionalProductos = productosRepository.findById(id);
        if (optionalProductos.isPresent()) {
            productosRepository.delete(optionalProductos.get());
        } else {
            throw new EntityNotFoundException("Producto no encontrado con el ID: " + id);
        }
    }
}
