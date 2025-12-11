package ar.utn.frbb.tup.spboot_demo.persistence;

import ar.utn.frbb.tup.spboot_demo.model.Cuenta;
import ar.utn.frbb.tup.spboot_demo.persistence.entity.ClienteEntity;
import ar.utn.frbb.tup.spboot_demo.persistence.entity.CuentaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CuentaDao  extends AbstractBaseDao{

    @Autowired
    ClienteDao clienteDao;

    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        CuentaEntity entity = (CuentaEntity) getInMemoryDatabase().get(id);

        if (entity == null) {
            return null;
        }

        Cuenta cuenta = entity.toCuenta();

        if (entity.getTitular() != null) {
            cuenta.setTitular(clienteDao.find(entity.getTitular(), false));
        }

        return cuenta;
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();

        for (Object obj : getInMemoryDatabase().values()) {
            CuentaEntity entity = (CuentaEntity) obj;

            if (entity.getTitular().equals(dni)) {

                Cuenta cuenta = entity.toCuenta();

                cuenta.setTitular(clienteDao.find(dni, false));

                cuentasDelCliente.add(cuenta);
            }
        }

        return cuentasDelCliente;
    }
}
