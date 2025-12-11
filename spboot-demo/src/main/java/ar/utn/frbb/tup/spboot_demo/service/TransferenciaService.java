package ar.utn.frbb.tup.spboot_demo.service;

import ar.utn.frbb.tup.spboot_demo.model.Cuenta;
import ar.utn.frbb.tup.spboot_demo.model.TipoMoneda;
import ar.utn.frbb.tup.spboot_demo.model.Transaccion;
import ar.utn.frbb.tup.spboot_demo.model.dto.ResTransferencia;
import ar.utn.frbb.tup.spboot_demo.model.dto.SolTransferencia;
import ar.utn.frbb.tup.spboot_demo.persistence.ClienteDao;
import ar.utn.frbb.tup.spboot_demo.persistence.CuentaDao;
import ar.utn.frbb.tup.spboot_demo.persistence.TransaccionDao;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {
    private final CuentaDao cuentaDao;
    private final ClienteDao clienteDao;
    private final BanelcoService banelcoService;
    private final TransaccionDao transaccionDao;

    public TransferenciaService(CuentaDao cuentaDao, ClienteDao clienteDao, BanelcoService banelcoService, TransaccionDao transaccionDao) {
        this.cuentaDao = cuentaDao;
        this.clienteDao = clienteDao;
        this.banelcoService = banelcoService;
        this.transaccionDao = transaccionDao;
    }

    public ResTransferencia realizarTransferencia(SolTransferencia solicitud) {
        Cuenta origen = cuentaDao.find(solicitud.getCuentaOrigen());

        if (origen != null && origen.getTitular() != null) {
            var titular = clienteDao.find(origen.getTitular().getDni(), false);
            origen.setTitular(titular);
        }

        Cuenta destino = cuentaDao.find(solicitud.getCuentaDestino());

        if (destino != null && destino.getTitular() != null) {
            var titularDestino = clienteDao.find(destino.getTitular().getDni(), false);
            destino.setTitular(titularDestino);
        }

        if (origen == null) {
            return new ResTransferencia("FALLIDA", "La cuenta origen no existe");
        }

        TipoMoneda monedaTrasnferencia;

        try{
            monedaTrasnferencia = TipoMoneda.valueOf(solicitud.getMoneda().toUpperCase());
        } catch (Exception e){
            return new ResTransferencia("FALLIDA", "La moneda de la cuenta origen no coincide con la transferencia");
        }

        boolean destinoOtroBanco = false;

        if (destino == null) {
            destinoOtroBanco = true;
        } else {
            if (destino.getMoneda() != monedaTrasnferencia) {
                return new ResTransferencia("FALLIDA", "La moneda de ambas cuentas debe ser la misma");
            }
        }

        double monto = solicitud.getMonto();
        double comision = calcularComision(monto, monedaTrasnferencia);
        double totalDebitar = monto + comision;

        if (origen.getBalance() < totalDebitar) {
            return new ResTransferencia("FALLIDA", "Fondos insuficientes para realizar la transferencia. Se requiere: " + totalDebitar);
        }

        origen.setBalance(origen.getBalance() - totalDebitar);
        cuentaDao.save(origen);

        if (!destinoOtroBanco) {
            destino.setBalance(destino.getBalance() + monto);
            cuentaDao.save(destino);
            transaccionDao.save(origen.getNumeroCuenta(), new Transaccion("SALIENTE", "Transferencia enviada a " + destino.getNumeroCuenta(), monto));
            transaccionDao.save(destino.getNumeroCuenta(), new Transaccion("ENTRANTE", "Transferencia recibida de " + origen.getNumeroCuenta(), monto));
        } else {
            boolean ok = banelcoService.enviarTransferencia(solicitud.getCuentaDestino(), monto);

            if (!ok) {
                return new ResTransferencia("FALLIDA", "El servicio Banelco rechazó la transferencia");
            }

            transaccionDao.save(origen.getNumeroCuenta(), new Transaccion("SALIENTE", "Transferencia enviada a otro banco (cuenta " + solicitud.getCuentaDestino() + ")", monto));
        }

        return new ResTransferencia("EXITOSA", "Transferencia realizada exitosamente. Comision: " + comision);
    }

    private double calcularComision(double monto, TipoMoneda moneda) {
        if (moneda == TipoMoneda.PESOS && monto > 1000000) {
            return monto * 0.02; // 2%
        }
        if (moneda == TipoMoneda.DOLARES && monto > 5000) {
            return monto * 0.005; // 0.5%
        }
        return 0;
    }
}
