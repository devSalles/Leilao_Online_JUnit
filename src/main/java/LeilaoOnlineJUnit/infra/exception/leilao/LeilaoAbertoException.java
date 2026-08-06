package LeilaoOnlineJUnit.infra.exception.leilao;

public class LeilaoAbertoException extends RuntimeException {
    public LeilaoAbertoException(String message) {
        super(message);
    }
    public LeilaoAbertoException() {
        super("Leilão já está aberto");
    }
}
