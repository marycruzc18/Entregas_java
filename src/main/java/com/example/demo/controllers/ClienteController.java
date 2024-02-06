package com.example.demo.controllers;

import com.example.demo.models.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String index(){
        return "Conectado";
    }

    //Muestra la información del cliente con la edad

    @GetMapping("/info")
    public ResponseEntity<?> obtenerInfoCliente() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();

            if (!clientes.isEmpty()) {
                for (Cliente cliente : clientes) {
                    int edad = clienteService.calcularEdad(cliente.getFechaNacimiento());
                    cliente.setEdad(edad);
                }
                return new ResponseEntity<>(clientes, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No hay clientes en la base de datos", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener información de los clientes: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Obtener todos los clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes(){
        List<Cliente> clientes = clienteService.obtenerTodosClientes();
        return ResponseEntity.ok(clientes);
    }


    //Guardar los clientes en la base de datos
    @PostMapping("/alta")
    public ResponseEntity<String> guardarCliente(@RequestBody Cliente cliente) {
        clienteService.guardarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente guardado exitosamente");
    }
    //Modificar cliente
    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            clienteService.modificarCliente(id, cliente);
            return ResponseEntity.ok("Cliente actualizado exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Eliminar clientes

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.ok("Cliente eliminado exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
