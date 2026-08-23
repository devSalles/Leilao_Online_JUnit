package LeilaoOnlineJUnit.infra.exception;

public class PossuiItemEmLeilaoException extends RuntimeException {
    public PossuiItemEmLeilaoException(String message) {
        super(message);
    }
    public PossuiItemEmLeilaoException() {
        super("Usuário possui item vinculados a leilão");
    }
}
