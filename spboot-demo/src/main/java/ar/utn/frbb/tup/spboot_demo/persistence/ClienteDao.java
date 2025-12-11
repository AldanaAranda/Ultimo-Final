package ar.utn.frbb.tup.spboot_demo.persistence;

import ar.utn.frbb.tup.spboot_demo.model.Cliente;
import ar.utn.frbb.tup.spboot_demo.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao{

    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null){
            return null;
        }

        return ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();
    }

    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
