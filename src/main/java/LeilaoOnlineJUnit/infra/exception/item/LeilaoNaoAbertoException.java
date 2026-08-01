package LeilaoOnlineJUnit.infra.exception.item;

public class LeilaoNaoAbertoException extends RuntimeException {
    public LeilaoNaoAbertoException(String message) {
        super(message);
    }
    public LeilaoNaoAbertoException() {
        super("Somente leilões abertos aceitam novos lances");
    }
}
