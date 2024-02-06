package com.example.demo.controllers;

import com.example.demo.models.Productos;
import com.example.demo.repository.ProductosRepository;
import com.example.demo.service.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductosController {

    @Autowired
    private ProductosService productosService;

    //Obtener todos los productos
    @GetMapping("productos")
    public ResponseEntity<List<Productos>> getProductos() {
        List<Productos> productos = productosService.obtenerTodosProductos();
        return ResponseEntity.ok(productos);
    }

    //Guardar los productos en la base de datos
    @PostMapping("altaproductos")
    public ResponseEntity<String> guardarProducto(@RequestBody Productos producto) {
        productosService.guardarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto guardado exitosamente");
    }

    //Modificar productos

    @PutMapping("modificarproductos/{id}")
    public ResponseEntity<String> modificarProducto(@PathVariable Long id, @RequestBody Productos producto) {
        try {
            productosService.modificarProducto(id, producto);
            return ResponseEntity.ok("Producto modificado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo modificar el producto: " + e.getMessage());
        }
    }

    //eliminar productos
    @DeleteMapping("bajaproductos/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        try {
            productosService.eliminarProducto(id);
            return ResponseEntity.ok("Producto eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo eliminar el producto: " + e.getMessage());
        }
    }
}