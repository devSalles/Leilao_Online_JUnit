package LeilaoOnlineJUnit.infra.exception;

public class ItemEmLeilaoException extends RuntimeException {
    public ItemEmLeilaoException(String message) {
        super(message);
    }
    public ItemEmLeilaoException() {
        super("Um item em leilão não pode ser editado");
    }
}
