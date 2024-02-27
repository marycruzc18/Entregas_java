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
    private ClienteService clienteService;

    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteController(ClienteService clienteService , ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public String index(){
        return "Conectado";
    }



    //Obtener todos los clientes
    @GetMapping("/todosclientes")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes(){
        List<Cliente> clientes = clienteService.obtenerTodosClientes();
        return ResponseEntity.ok(clientes);
    }

    //Buscar los clientes por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable Long id) {
        try {
            // Buscar el cliente por ID en la base de datos
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));

            // Devolver el cliente encontrado en la respuesta
            return ResponseEntity.ok(cliente);
        } catch (EntityNotFoundException e) {
            // Manejar el caso donde no se encontró el cliente
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Manejar cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el cliente: " + e.getMessage());
        }
    }


//Guardar los clientes en la base de datos
@PostMapping("/alta")
public ResponseEntity<?> agregarCliente(@RequestBody Cliente cliente) {
    try {
        clienteService.guardarCliente(cliente);
        return new ResponseEntity<>("Cliente agregado correctamente", HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>("Error al agregar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
