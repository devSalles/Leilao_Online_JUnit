package LeilaoOnlineJUnit.infra.exception;

public class PrimeiroLanceInvaidoException extends RuntimeException {
    public PrimeiroLanceInvaidoException(String message) {
        super(message);
    }
    public PrimeiroLanceInvaidoException() {
        super("Valor do primeiro lance deve ser maior ou igual que valor inicial do item");
    }
}
