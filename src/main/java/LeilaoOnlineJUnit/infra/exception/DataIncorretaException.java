package LeilaoOnlineJUnit.infra.exception;

public class DataIncorretaException extends RuntimeException {
    public DataIncorretaException(String message) {
        super(message);
    }
    public DataIncorretaException() {
        super("Datas incorretas");
    }
}
