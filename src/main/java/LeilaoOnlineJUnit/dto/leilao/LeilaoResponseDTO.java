package LeilaoOnlineJUnit.dto.leilao;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.entity.Leilao;

import java.time.LocalDateTime;

public record LeilaoResponseDTO(
        Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        StatusLeilao statusLeilao,
        Long idItem,
        Long idCriador
) {

    public static LeilaoResponseDTO fromLeilao(Leilao leilao)
    {
        return new LeilaoResponseDTO(leilao.getId(), leilao.getDataInicio(),leilao.getDataFim(),leilao.getStatusLeilao(),
                leilao.getItem().getId(),leilao.getCriador().getId());
    }
}
