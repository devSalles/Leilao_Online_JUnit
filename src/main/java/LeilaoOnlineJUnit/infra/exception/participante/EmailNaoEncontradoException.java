package LeilaoOnlineJUnit.infra.exception.participante;

public class EmailNaoEncontradoException extends RuntimeException {
    public EmailNaoEncontradoException(String message) {
        super(message);
    }
    public EmailNaoEncontradoException() {
        super("Email não encontrado");
    }
}
