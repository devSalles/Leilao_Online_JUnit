package LeilaoOnlineJUnit.infra.exception;

public class PossuiLeilaoAtivoException extends RuntimeException {
    public PossuiLeilaoAtivoException(String message) {
        super(message);
    }
    public PossuiLeilaoAtivoException() {
        super("Usuário possui leilão ativo");
    }
}
