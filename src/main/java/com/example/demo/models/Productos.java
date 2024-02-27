package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column
    private String descripcion;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column
    private int stock;

    @Column
    private double precio;



    //se crean los getters y setters

    public Productos() {
        // Constructor vacío
    }

    public Productos(String descripcion, String codigo, int stock, double precio) {
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.stock = stock;
        this.precio = precio;

    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }




    @Override
    public String toString() {
        return "Productos{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", codigo='" + codigo + '\'' +
                ", stock=" + stock +
                ", precio=" + precio +
                '}';
    }


}
