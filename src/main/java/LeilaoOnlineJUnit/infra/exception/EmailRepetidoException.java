package LeilaoOnlineJUnit.infra.exception;

public class EmailRepetidoException extends RuntimeException {
    public EmailRepetidoException(String message) {
        super(message);
    }
    public EmailRepetidoException() {
        super("Email já cadastrado");
    }
}
