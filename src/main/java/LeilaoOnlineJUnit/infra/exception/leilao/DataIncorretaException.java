package LeilaoOnlineJUnit.infra.exception.item;

public class DataIncorretaException extends RuntimeException {
    public DataIncorretaException(String message) {
        super(message);
    }
    public DataIncorretaException() {
        super("Datas incorretas");
    }
}
