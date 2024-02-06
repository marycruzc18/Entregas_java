package com.example.demo.service;

import com.example.demo.models.Cliente;
import com.example.demo.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    public Cliente obtenerClienteConEdad(Cliente cliente) {
        int edad = calcularEdad(cliente.getFechaNacimiento());
        cliente.setEdad(edad);
        return cliente;
    }

    public int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento != null) {
            LocalDate fechaActual = LocalDate.now();
            return Period.between(fechaNacimiento, fechaActual).getYears();
        } else {
            // Aquí se retorna -1 como indicador de que no se pudo calcular la edad.
            return -1;
        }
    }

    public List<Cliente> obtenerTodosClientes(){
        return clienteRepository.findAll();
    }

    public void guardarCliente(Cliente cliente){
        clienteRepository.save(cliente);
    }

    public void modificarCliente(Long id, Cliente cliente) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            Cliente updateCliente = optionalCliente.get();
            updateCliente.setNombre(cliente.getNombre());
            updateCliente.setApellido(cliente.getApellido());
            updateCliente.setFechaNacimiento(cliente.getFechaNacimiento());
            clienteRepository.save(updateCliente);
        } else {
            throw new EntityNotFoundException("Cliente no encontrado con el ID: " + id);
        }
    }

    public void eliminarCliente(Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            clienteRepository.delete(optionalCliente.get());
        } else {
            throw new EntityNotFoundException("Cliente no encontrado con el ID: " + id);
        }
    }
}
