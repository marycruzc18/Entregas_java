package com.example.demo.service;


import com.example.demo.dto.DetalleVentaRequest;
import com.example.demo.dto.NuevaVentaRequest;
import com.example.demo.models.Cliente;
import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Productos;
import com.example.demo.models.Ventas;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ProductosRepository;
import com.example.demo.repository.VentasRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentasService {

    private final VentasRepository ventasRepository;
    private final ClienteRepository clienteRepository;

    private final ProductosRepository productosRepository;

    @Autowired
    public VentasService(VentasRepository ventasRepository, ClienteRepository clienteRepository,ProductosRepository productosRepository) {
        this.ventasRepository = ventasRepository;
        this.clienteRepository = clienteRepository;
        this.productosRepository =  productosRepository;
    }


    public List<Ventas> obtenerTodasLasVentas() {
        return ventasRepository.findAll();
    }

    public Optional<Ventas> obtenerVentaPorId(Long id) {
        return ventasRepository.findById(id);
    }


    @Transactional
    public Ventas crearVenta(NuevaVentaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        Ventas venta = new Ventas();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());

        // Procesar los detalles de la venta
        double totalVenta = 0.0; // Inicializar el total de la venta
        for (DetalleVentaRequest detalleRequest : request.getDetalles()) {
            // Verificar la existencia del producto
            Productos producto = productosRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            // Verificar el stock disponible
            if (detalleRequest.getCantidad() > producto.getStock()) {
                throw new IllegalStateException("Cantidad solicitada mayor que el stock disponible para el producto: " + producto.getDescripcion());
            }

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setCantidad(detalleRequest.getCantidad());
            detalleVenta.setPrecioUnitario(detalleRequest.getPrecioUnitario());
            detalleVenta.setProducto(producto); // Asignar el producto al detalle de venta
            detalleVenta.setVenta(venta); // Asociar el detalle de venta con la venta
            venta.getDetalleVenta().add(detalleVenta); // Agregar el detalle de venta a la lista de detalles de la venta

            // Calcular el subtotal del detalle de venta y sumarlo al total de la venta
            totalVenta += detalleVenta.getSubtotal();

            // Reducir el stock del producto
            producto.setStock(producto.getStock() - detalleVenta.getCantidad());
            productosRepository.save(producto);
        }

        // Asignar el total de la venta
        venta.setTotal(totalVenta);



        // Guardar la venta en la base de datos
        return ventasRepository.save(venta);
    }



    public void eliminarVenta(Long id) {
        ventasRepository.deleteById(id);
    }




}
