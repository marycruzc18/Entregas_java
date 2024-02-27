package com.example.demo.controllers;


import com.example.demo.dto.DetalleVentaRequest;
import com.example.demo.dto.NuevaVentaRequest;
import com.example.demo.models.Cliente;
import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Productos;
import com.example.demo.models.Ventas;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ProductosRepository;
import com.example.demo.repository.VentasRepository;
import com.example.demo.service.ComprobanteService;
import com.example.demo.service.VentasService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
public class VentasController {

    private final ClienteRepository clienteRepository;
    private final VentasRepository ventasRepository;

    private final ProductosRepository productosRepository;

    private final ComprobanteService comprobanteService;

    private final VentasService ventasService;

    @Autowired
    public VentasController(ClienteRepository clienteRepository, VentasRepository ventasRepository, ProductosRepository productosRepository, ComprobanteService comprobanteService, VentasService ventasService) {
        this.clienteRepository = clienteRepository;
        this.ventasRepository = ventasRepository;
        this.productosRepository = productosRepository;
        this.comprobanteService = comprobanteService;
        this.ventasService = ventasService;
    }

    @Operation(summary = "Crear una nueva venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al crear la venta")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearVenta(@RequestBody NuevaVentaRequest request) {
        try {
            // Obtener el cliente desde la base de datos
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

            // Crear la venta
            Ventas venta = new Ventas();
            venta.setCliente(cliente);

            // Utilizar el servicio de Comprobante para obtener la fecha del comprobante
            venta.setFecha(comprobanteService.obtenerFechaComprobante());

            // Lista para almacenar los productos cuyo stock debe actualizarse
            List<Productos> productosActualizarStock = new ArrayList<>();

            // Procesar los detalles de la venta
            double totalVenta = 0.0; // Inicializar el total de la venta
            for (DetalleVentaRequest detalleRequest : request.getDetalles()) {
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setCantidad(detalleRequest.getCantidad());

                // Obtener el producto desde la base de datos
                Productos producto = productosRepository.findById(detalleRequest.getProductoId())
                        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

                detalleVenta.setProducto(producto); // Asignar el producto al detalle de venta
                detalleVenta.setPrecioProducto(producto.getPrecio()); // Asignar el precio actual del producto

                // Calcular el precio unitario y el subtotal
                double precioUnitario = producto.getPrecio();
                detalleVenta.setPrecioUnitario(precioUnitario);
                double subtotal = precioUnitario * detalleVenta.getCantidad();
                detalleVenta.setSubtotal(subtotal);

                // Almacenar el detalle de venta en la venta
                venta.agregarDetalleVenta(detalleVenta);

                // Calcular el total de la venta
                totalVenta += subtotal;

                // Verificar si la cantidad vendida es mayor que el stock actual
                if (detalleVenta.getCantidad() > producto.getStock()) {
                    return ResponseEntity.badRequest().body("Cantidad vendida mayor que el stock actual para el producto: " + producto.getDescripcion());
                }

                // Agregar el producto a la lista para actualizar su stock
                productosActualizarStock.add(producto);
            }

            // Asignar el total de la venta
            venta.setTotal(totalVenta);

            // Guardar la venta en la base de datos
            venta = ventasRepository.save(venta);

            // Actualizar el stock de productos
            for (Productos producto : productosActualizarStock) {
                DetalleVenta detalleVenta = venta.getDetalleVenta().stream()
                        .filter(detalle -> detalle.getProducto().equals(producto))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No se encontró el detalle de venta para el producto: " + producto.getDescripcion()));

                int nuevoStock = producto.getStock() - detalleVenta.getCantidad();
                producto.setStock(nuevoStock);
            }

            // Guardar todos los productos actualizados en la base de datos
            productosRepository.saveAll(productosActualizarStock);

            // Devolver una respuesta exitosa con el ID de la venta
            return ResponseEntity.ok("Venta creada exitosamente con ID: " + venta.getId());
        } catch (EntityNotFoundException e) {
            // Manejar el caso donde no se encontró el cliente o el producto
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al crear la venta: " + e.getMessage());
        } catch (Exception e) {
            // Manejar cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la venta: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener todas las ventas")
    @ApiResponse(responseCode = "200", description = "Muestra todas las ventas")
    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodasLasVentas() {
        try {
            List<Ventas> ventas = ventasService.obtenerTodasLasVentas();
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener todas las ventas: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener una venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVentaPorId(@PathVariable Long id) {
        try {
            Optional<Ventas> ventaOptional = ventasService.obtenerVentaPorId(id);
            if (ventaOptional.isPresent()) {
                Ventas venta = ventaOptional.get();
                return ResponseEntity.ok(venta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la venta: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVenta(@PathVariable Long id) {
        try {
            ventasService.eliminarVenta(id);
            return ResponseEntity.ok("Venta eliminada exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @Operation(summary = "Construir comprobante de venta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se muestra el comprobante"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}/comprobante")
    private String construirComprobante(@PathVariable Long id) {
        // Buscar la venta por ID
        Optional<Ventas> ventaOptional = ventasService.obtenerVentaPorId(id);
        if (ventaOptional.isPresent()) {
            Ventas venta = ventaOptional.get();
            // Construir el comprobante utilizando los datos de la venta
            StringBuilder sb = new StringBuilder();
            sb.append("Comprobante de Venta\n");
            sb.append("ID de Venta: ").append(venta.getId()).append("\n");
            sb.append("Fecha: ").append(venta.getFecha()).append("\n");
            sb.append("Cliente: ").append(venta.getCliente().getNombre()).append("\n");
            sb.append("Detalles:\n");
            for (DetalleVenta detalle : venta.getDetalleVenta()) {
                sb.append("Producto: ").append(detalle.getProducto().getDescripcion()).append("\n");
                sb.append("Cantidad: ").append(detalle.getCantidad()).append("\n");
                sb.append("Precio Unitario: ").append(detalle.getPrecioProducto()).append("\n");
                sb.append("Subtotal: ").append(detalle.getCantidad() * detalle.getPrecioProducto()).append("\n");
                sb.append("\n");
            }
            sb.append("Total de Venta: ").append(venta.getTotal()).append("\n");
            return sb.toString();
        } else {
            return "Venta no encontrada";
        }
    }
}