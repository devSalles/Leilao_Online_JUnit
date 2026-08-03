package LeilaoOnlineJUnit.infra.exception.leilao;

public class DataIncorretaException extends RuntimeException {
    public DataIncorretaException(String message) {
        super(message);
    }
    public DataIncorretaException() {
        super("Datas incorretas");
    }
}
