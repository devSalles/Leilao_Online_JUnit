package LeilaoOnlineJUnit.infra.exception;

public class LeilaoNaoAbertoException extends RuntimeException {
    public LeilaoNaoAbertoException(String message) {
        super(message);
    }
    public LeilaoNaoAbertoException() {
        super("Somente leilões abertos aceitam novos lances");
    }
}
