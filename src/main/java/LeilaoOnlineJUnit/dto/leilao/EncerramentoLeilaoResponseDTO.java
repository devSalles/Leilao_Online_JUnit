package LeilaoOnlineJUnit.dto.leilao;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.entity.Leilao;

import java.time.LocalDateTime;

public record EncerramentoLeilaoResponseDTO(
        Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        StatusLeilao statusLeilao,
        StatusItem statusItem,
        Long idItem,
        Long idCriador,
        Long idVencedor
) {

    public static EncerramentoLeilaoResponseDTO fromLeilao(Leilao leilao)
    {
        Long idVencedor = null;

        if (leilao.getVencedor() != null)
        {
            idVencedor = leilao.getVencedor().getId();
        }

        return new EncerramentoLeilaoResponseDTO(leilao.getId(), leilao.getDataInicio(),leilao.getDataFim(),leilao.getStatusLeilao(),
                leilao.getItem().getStatusItem(), leilao.getItem().getId(),leilao.getCriador().getId(),idVencedor);
    }
}
