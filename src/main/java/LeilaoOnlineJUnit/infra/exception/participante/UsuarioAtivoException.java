package LeilaoOnlineJUnit.infra.exception.participante;

public class UsuarioAtivoException extends RuntimeException {
    public UsuarioAtivoException(String message) {
        super(message);
    }
    public UsuarioAtivoException() {
        super("Usuário já está ativo");
    }
}
