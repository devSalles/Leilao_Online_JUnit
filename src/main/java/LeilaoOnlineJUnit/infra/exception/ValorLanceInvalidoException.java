package LeilaoOnlineJUnit.infra.exception;

public class ValorLanceInvalidoException extends RuntimeException {
    public ValorLanceInvalidoException(String message) {
        super(message);
    }
    public ValorLanceInvalidoException() {
        super("Valor do lance não pode ser abaixo do valor do item");
    }
}
