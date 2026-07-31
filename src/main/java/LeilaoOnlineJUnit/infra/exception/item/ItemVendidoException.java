package LeilaoOnlineJUnit.infra.exception.item;

public class ItemVendidoException extends RuntimeException {
    public ItemVendidoException(String message) {
        super(message);
    }
    public ItemVendidoException() {
        super("Item vendido não pode ser editado");
    }
}
