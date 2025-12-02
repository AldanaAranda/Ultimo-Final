package ar.utn.frbb.tup.spboot_demo.controller;

import ar.utn.frbb.tup.spboot_demo.model.Cuenta;
import ar.utn.frbb.tup.spboot_demo.model.exception.TipoCuentaAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    private final ClienteService clienteService;

    public CuentaController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> crearCuenta(
            @RequestParam long dni,
            @RequestBody Cuenta cuenta
    ) {
        try {
            clienteService.agregarCuenta(cuenta, dni);
            return ResponseEntity.ok("Cuenta creada exitosamente");
        }
        catch (Exception | TipoCuentaAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
