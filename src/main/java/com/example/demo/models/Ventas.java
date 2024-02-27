package com.example.demo.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Ventas {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @JsonManagedReference // Evita la recursión infinita al serializar
    private List<DetalleVenta> detalleVenta;

    private LocalDateTime fecha;

    @Column(name = "total")
    private double total;

    // Constructor
    public Ventas() {
        this.detalleVenta = new ArrayList<>();
    }

    // Método para agregar un detalle de venta
    public void agregarDetalleVenta(DetalleVenta detalleVenta) {
        detalleVenta.setVenta(this); // Establecer la relación inversa
        this.detalleVenta.add(detalleVenta);
    }

    // Método para calcular la cantidad total de productos vendidos
    public int getCantidadTotal() {
        return detalleVenta.stream().mapToInt(DetalleVenta::getCantidad).sum();
    }

    // Método para calcular el total de la venta
    public double getTotalVenta() {
        double totalVenta = detalleVenta.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
        this.total = totalVenta; // Asignar el total calculado al atributo total de la venta
        return totalVenta;
    }


    //Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }




    public List<DetalleVenta> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<DetalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

