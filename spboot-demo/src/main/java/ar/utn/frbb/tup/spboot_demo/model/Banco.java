package ar.utn.frbb.tup.spboot_demo.model;

import java.util.ArrayList;
import java.util.List;


public class Banco {
    private List<Cliente> clientes = new ArrayList<>();

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
