package ar.utn.frbb.tup.spboot_demo.controller;

import ar.utn.frbb.tup.spboot_demo.model.Cliente;
import ar.utn.frbb.tup.spboot_demo.model.exception.ClienteAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
        try{
            clienteService.darDeAltaCliente(cliente);
            return ResponseEntity.ok("Cliente creado exitosamente");
        }
        catch (Exception | ClienteAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
