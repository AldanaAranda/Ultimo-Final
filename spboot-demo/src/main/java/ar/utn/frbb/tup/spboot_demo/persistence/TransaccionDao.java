package ar.utn.frbb.tup.spboot_demo.persistence;

import ar.utn.frbb.tup.spboot_demo.model.Transaccion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransaccionDao {
    private final Map<Long, List<Transaccion>> historial = new HashMap<>();

    public void save(long numeroCuenta, Transaccion transaccion) {
        historial.computeIfAbsent(numeroCuenta, k -> new ArrayList<>()).add(transaccion);
    }

    public List<Transaccion> getByCuenta(long numeroCuenta) {
        return historial.getOrDefault(numeroCuenta, new ArrayList<>());
    }
}
