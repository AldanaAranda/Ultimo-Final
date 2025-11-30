package ar.utn.frbb.tup.spboot_demo.model.exception;

public class CuentaAlreadyExistsException extends Throwable{
    public CuentaAlreadyExistsException(String message) {
        super(message);
    }
}
