package ar.utn.frbb.tup.spboot_demo.model.dto;

import ar.utn.frbb.tup.spboot_demo.model.Cuenta;

public class ResCuentaCreada {
    private String mensaje;
    private Cuenta cuenta;

    public ResCuentaCreada(String mensaje, Cuenta cuenta) {
        this.mensaje = mensaje;
        this.cuenta = cuenta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }
}
