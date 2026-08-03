package LeilaoOnlineJUnit.dto.leilao;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record LeilaoRequestDTO(

    @NotNull(message = "A data de início e obrigatória")
    @Future(message = "A data de início deve ser futura")
    LocalDateTime dataInicio,

    @NotNull(message = "A data final e obrigatória")
    @Future(message = "A data final deve ser futura")
    LocalDateTime dataFim,

    @NotNull(message = "O ID do item e obrigatório")
    Long idItem,

    @NotNull(message = "O ID do criador e obrigatório")
    Long idCriador
) {

    public Leilao toLeilao(Item item, Usuario criador)
    {
        Leilao leilao = new Leilao();

        leilao.setDataInicio(dataInicio);
        leilao.setDataFim(dataFim);
        leilao.setStatusLeilao(StatusLeilao.AGENDADO);
        leilao.setCriador(criador);
        leilao.setItem(item);

        return leilao;
    }
}
