package LeilaoOnlineJUnit.infra.exception;

public class ItemComProprietarioVinculadoException extends RuntimeException {
    public ItemComProprietarioVinculadoException(String message) {
        super(message);
    }
    public ItemComProprietarioVinculadoException() {
        super("Esse item já possui proprietário vinculado");
    }
}
