package ar.utn.frbb.tup.spboot_demo.model.dto;

public class ResTransferencia {
    private String estado;
    private String mensaje;

    public ResTransferencia(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
