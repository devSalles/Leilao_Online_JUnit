package LeilaoOnlineJUnit.infra.exception.item;

public class ItemComProprietarioVinculadoException extends RuntimeException {
    public ItemComProprietarioVinculadoException(String message) {
        super(message);
    }
    public ItemComProprietarioVinculadoException() {
        super("Esse item já possui proprietário vinculado");
    }
}
