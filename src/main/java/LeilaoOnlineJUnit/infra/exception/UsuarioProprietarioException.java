package LeilaoOnlineJUnit.infra.exception;

public class UsuarioProprietarioException extends RuntimeException {
    public UsuarioProprietarioException(String message) {
        super(message);
    }
    public UsuarioProprietarioException() {
        super("Usuário proprietário não pode realizar lances");
    }
}
