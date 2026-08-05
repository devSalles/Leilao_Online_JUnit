package LeilaoOnlineJUnit.infra.exception.leilao;

public class StatusDeLeilaoIncorretoException extends RuntimeException {
    public StatusDeLeilaoIncorretoException(String message) {
        super(message);
    }
    public StatusDeLeilaoIncorretoException() {
        super("Apenas leilões com status de AGENDADO podem ser atualizados");
    }
}
