package ar.utn.frbb.tup.spboot_demo.model.dto;

import ar.utn.frbb.tup.spboot_demo.model.Transaccion;

import java.util.List;

public class HistorialDto {
    private long numeroCuenta;
    private List<Transaccion> transacciones;

    public HistorialDto(long numeroCuenta, List<Transaccion> transacciones) {
        this.numeroCuenta = numeroCuenta;
        this.transacciones = transacciones;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }
}
