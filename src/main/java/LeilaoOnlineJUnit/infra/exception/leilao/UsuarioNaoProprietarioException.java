package LeilaoOnlineJUnit.infra.exception.leilao;

public class UsuarioNaoProprietarioException extends RuntimeException {
    public UsuarioNaoProprietarioException(String message) {
        super(message);
    }
    public UsuarioNaoProprietarioException() {
        super("O usuário não e proprietário do item");
    }
}
