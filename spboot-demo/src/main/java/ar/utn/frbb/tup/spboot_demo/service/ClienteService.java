package ar.utn.frbb.tup.spboot_demo.service;

import ar.utn.frbb.tup.spboot_demo.model.Cliente;
import ar.utn.frbb.tup.spboot_demo.model.Cuenta;
import ar.utn.frbb.tup.spboot_demo.model.exception.ClienteAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.model.exception.TipoCuentaAlreadyExistsException;
import ar.utn.frbb.tup.spboot_demo.persistence.ClienteDao;
import ar.utn.frbb.tup.spboot_demo.persistence.CuentaDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClienteService {

    ClienteDao clienteDao;
    CuentaDao cuentaDao;

    public ClienteService(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public void darDeAltaCliente(Cliente cliente) throws ClienteAlreadyExistsException {

        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }


        if(cliente.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        if (cliente.getBanco() == null) {
            cliente.setBanco("No informado");
        }

        cliente.setFechaAlta(LocalDate.now());
        clienteDao.save(cliente);
    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException {
        Cliente titular = buscarClientePorDni(dniTitular);
        cuenta.setDniTitular(dniTitular);
        cuenta.setTitular(titular);
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }
        titular.addCuenta(cuenta);
        clienteDao.save(titular);
    }

    public Cliente buscarClientePorDni(long dni) {
        Cliente cliente = clienteDao.find(dni, false);
        if(cliente == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        cliente.getCuentas().clear();
        cliente.getCuentas().addAll(cuentaDao.getCuentasByCliente(dni));
        return cliente;
    }
}
