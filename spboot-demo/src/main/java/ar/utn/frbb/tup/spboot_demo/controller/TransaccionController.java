package ar.utn.frbb.tup.spboot_demo.controller;

import ar.utn.frbb.tup.spboot_demo.model.dto.HistorialDto;
import ar.utn.frbb.tup.spboot_demo.persistence.TransaccionDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuenta")
public class TransaccionController {
    private final TransaccionDao transaccionDao;

    public TransaccionController(TransaccionDao transaccionDao) {
        this.transaccionDao = transaccionDao;
    }

    @GetMapping("/{numeroCuenta}/transacciones")
    public HistorialDto obtenerHistorial(@PathVariable long numeroCuenta) {
        var historial = transaccionDao.getByCuenta(numeroCuenta);

        return new HistorialDto(numeroCuenta, historial);
    }
}
