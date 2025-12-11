package ar.utn.frbb.tup.spboot_demo.controller;

import ar.utn.frbb.tup.spboot_demo.model.dto.ResTransferencia;
import ar.utn.frbb.tup.spboot_demo.model.dto.SolTransferencia;
import ar.utn.frbb.tup.spboot_demo.service.TransferenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferenciaController {
    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    public ResponseEntity<ResTransferencia> transferir(@RequestBody SolTransferencia solicitud){
        ResTransferencia respuesta = transferenciaService.realizarTransferencia(solicitud);

        if (respuesta.getEstado().equals("FALLIDA")) {
            return ResponseEntity.badRequest().body(respuesta);
        }

        return ResponseEntity.ok(respuesta);
    }
}
