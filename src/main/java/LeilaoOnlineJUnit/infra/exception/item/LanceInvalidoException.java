package LeilaoOnlineJUnit.infra.exception.item;

public class LanceInvalidoException extends RuntimeException {
    public LanceInvalidoException(String message) {
        super(message);
    }
    public LanceInvalidoException() {
        super("O lance deve ser maior que o lance atual");
    }
}
