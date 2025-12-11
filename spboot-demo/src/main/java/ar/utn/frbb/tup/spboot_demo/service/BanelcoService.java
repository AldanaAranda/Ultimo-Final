package ar.utn.frbb.tup.spboot_demo.service;

import org.springframework.stereotype.Service;

@Service
public class BanelcoService {
    public boolean enviarTransferencia(long cuentaDestino, double monto) {
        return Math.random() > 0.1;
    }
}
