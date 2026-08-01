package LeilaoOnlineJUnit.infra.exception.item;

public class PrimeiroLanceInvaidoException extends RuntimeException {
    public PrimeiroLanceInvaidoException(String message) {
        super(message);
    }
    public PrimeiroLanceInvaidoException() {
        super("Valor do primeiro lance deve ser maior que valor do item");
    }
}
