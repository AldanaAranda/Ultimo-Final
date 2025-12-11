package ar.utn.frbb.tup.spboot_demo.model;

import java.time.LocalDateTime;

public class Transaccion {
    private String fecha;
    private String tipo; // ENTRANTE / SALIENTE
    private String descripcion;
    private double monto;

    public Transaccion(String tipo, String descripcion, double monto) {
        this.fecha = LocalDateTime.now().toString();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getMonto() {
        return monto;
    }
}
