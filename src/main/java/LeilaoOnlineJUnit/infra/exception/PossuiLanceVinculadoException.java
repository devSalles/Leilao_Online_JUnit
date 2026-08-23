package LeilaoOnlineJUnit.infra.exception;

public class PossuiLanceVinculadoException extends RuntimeException {
    public PossuiLanceVinculadoException(String message) {
        super(message);
    }
    public PossuiLanceVinculadoException() {
        super("Leilão com lance vínculado não pode ser cancelado");
    }
}
