package ar.utn.frbb.tup.spboot_demo.controller;

import ar.utn.frbb.tup.spboot_demo.model.Cuenta;
import ar.utn.frbb.tup.spboot_demo.model.dto.ResCuentaCreada;
import ar.utn.frbb.tup.spboot_demo.model.exception.CuentaAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.model.exception.TipoCuentaAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.model.exception.TipoCuentaNoSoportadaException;
import ar.utn.frbb.tup.spboot_demo.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }


    @PostMapping
    public ResponseEntity<?> crearCuenta(@RequestBody Cuenta cuenta) {
        try {
            cuentaService.darDeAltaCuenta(cuenta, cuenta.getDniTitular());

            return ResponseEntity.ok(new ResCuentaCreada("Cuenta creada exitosamente", cuenta));
        }
        catch (CuentaAlreadyExistsException | TipoCuentaAlreadyExistsException | TipoCuentaNoSoportadaException e) {
            return ResponseEntity.badRequest().body("Error inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<?> obtenerCuenta(@PathVariable long numeroCuenta) {

        try {
            Cuenta cuenta = cuentaService.find(numeroCuenta);

            if (cuenta == null) {
                return ResponseEntity.badRequest().body("La cuenta no existe");
            }

            return ResponseEntity.ok(cuenta);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

    }

    @GetMapping("/cliente/{dni}")
    public ResponseEntity<?> obtenerCuentasDelCliente(@PathVariable long dni) {

        try {
            List<Cuenta> cuentas = cuentaService.obtenerCuentasPorCliente(dni);

            if (cuentas == null || cuentas.isEmpty()) {
                return ResponseEntity.badRequest().body("El cliente no tiene cuentas registradas");
            }

            return ResponseEntity.ok(cuentas);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

    }

}
