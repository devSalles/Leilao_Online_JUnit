package LeilaoOnlineJUnit.infra.exception;

public class ItemVinculadoAoLeilaoException extends RuntimeException {
    public ItemVinculadoAoLeilaoException(String message) {
        super(message);
    }
    public ItemVinculadoAoLeilaoException() {
        super("Item vínculado ao leilão não pode ser excluído");
    }
}
