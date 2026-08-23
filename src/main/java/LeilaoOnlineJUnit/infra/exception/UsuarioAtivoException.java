package LeilaoOnlineJUnit.infra.exception;

public class UsuarioAtivoException extends RuntimeException {
    public UsuarioAtivoException(String message) {
        super(message);
    }
    public UsuarioAtivoException() {
        super("Usuário já está ativo");
    }
}
