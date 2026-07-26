package LeilaoOnlineJUnit.infra.exception.participante;

public class CpfNaoEncontradoException extends RuntimeException {
    public CpfNaoEncontradoException(String message) {
        super(message);
    }
    public CpfNaoEncontradoException() {
        super("Cpf não encontrado");
    }
}
