package LeilaoOnlineJUnit.infra.exception;

public class LanceInvalidoException extends RuntimeException {
    public LanceInvalidoException(String message) {
        super(message);
    }
    public LanceInvalidoException() {
        super("O lance deve ser maior que o lance atual");
    }
}
