package ar.utn.frbb.tup.spboot_demo.service;

import ar.utn.frbb.tup.spboot_demo.model.Cliente;
import ar.utn.frbb.tup.spboot_demo.model.Cuenta;
import ar.utn.frbb.tup.spboot_demo.model.TipoCuenta;
import ar.utn.frbb.tup.spboot_demo.model.TipoMoneda;
import ar.utn.frbb.tup.spboot_demo.model.exception.CuentaAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.model.exception.TipoCuentaAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.model.exception.TipoCuentaNoSoportadaException;
import ar.utn.frbb.tup.spboot_demo.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CuentaService {

    @Autowired
    CuentaDao cuentaDao;
    @Autowired
    ClienteService clienteService;

    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        //Chequear cuentas soportadas por el banco CA$ CC$ CAU$S
        // if (!tipoCuentaEstaSoportada(cuenta)) {...}

        if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new TipoCuentaNoSoportadaException("El tipo de cuenta no es soportado.");
        }

        Cliente titular = clienteService.buscarClientePorDni(dniTitular);
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }

        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.save(cuenta);
    }

    private boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        TipoCuenta tipoCuenta = cuenta.getTipoCuenta();
        TipoMoneda tipoMoneda = cuenta.getMoneda();
        if (cuenta.getTipoCuenta() != null) {
            if (tipoCuenta == TipoCuenta.CAJA_AHORRO && tipoMoneda == TipoMoneda.PESOS) {
                return true;
            }

            if (tipoCuenta == TipoCuenta.CUENTA_CORRIENTE && tipoMoneda == TipoMoneda.PESOS) {
                return true;
            }

            if (tipoCuenta == TipoCuenta.CAJA_AHORRO && tipoMoneda == TipoMoneda.DOLARES) {
                return true;
            }
        }
        return false;
    }

    public Cuenta find ( long id){
            return cuentaDao.find(id);
        }

    public List<Cuenta> obtenerCuentasPorCliente(long dni) {
        return cuentaDao.getCuentasByCliente(dni);
    }
}
