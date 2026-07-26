package LeilaoOnlineJUnit.infra.exception.participante;

public class EmailRepetidoException extends RuntimeException {
    public EmailRepetidoException(String message) {
        super(message);
    }
    public EmailRepetidoException() {
        super("Email já cadastrado");
    }
}
