package LeilaoOnlineJUnit.infra.exception;

public class EmailNaoEncontradoException extends RuntimeException {
    public EmailNaoEncontradoException(String message) {
        super(message);
    }
    public EmailNaoEncontradoException() {
        super("Email não encontrado");
    }
}
