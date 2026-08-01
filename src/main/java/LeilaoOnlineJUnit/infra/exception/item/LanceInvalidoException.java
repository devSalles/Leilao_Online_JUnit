package LeilaoOnlineJUnit.infra.exception.item;

public class LanceInvalidoException extends RuntimeException {
    public LanceInvalidoException(String message) {
        super(message);
    }
    public LanceInvalidoException() {
        super("O novo lance deve ser maior que o maior lance registrado");
    }
}
