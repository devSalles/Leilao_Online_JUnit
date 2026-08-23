package LeilaoOnlineJUnit.infra.exception;

public class CpfRepetidoException extends RuntimeException {
    public CpfRepetidoException(String message) {
        super(message);
    }
    public CpfRepetidoException() {
        super("CPF já cadastrado");
    }
}
