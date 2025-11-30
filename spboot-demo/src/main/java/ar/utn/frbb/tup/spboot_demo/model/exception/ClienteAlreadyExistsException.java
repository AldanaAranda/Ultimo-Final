package ar.utn.frbb.tup.spboot_demo.model.exception;

public class ClienteAlreadyExistsException extends Throwable {
    public ClienteAlreadyExistsException(String message) {
        super(message);
    }
}
