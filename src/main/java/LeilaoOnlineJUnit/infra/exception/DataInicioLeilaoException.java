package LeilaoOnlineJUnit.infra.exception;

public class DataInicioLeilaoException extends RuntimeException {
    public DataInicioLeilaoException(String message) {
        super(message);
    }
    public DataInicioLeilaoException() {
        super("Leilão não pode ser aberto antes da data");
    }
}
