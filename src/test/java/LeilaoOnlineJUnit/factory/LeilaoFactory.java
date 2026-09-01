package LeilaoOnlineJUnit.factory;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class LeilaoFactory
{

    public Leilao criarLeilaoPronto(Item item, Usuario criador)
    {
        LocalDateTime dataHoraInicio = LocalDateTime.now();
        LocalDateTime dataHoraFim = LocalDateTime.now().plusDays(1);

        Leilao leilao = new Leilao();

        leilao.setId(1L);
        leilao.setDataInicio(dataHoraInicio);
        leilao.setDataFim(dataHoraFim);
        leilao.setStatusLeilao(StatusLeilao.ABERTO);
        leilao.setItem(item);
        leilao.setCriador(criador);

        return leilao;
    }

    public Leilao criarLeilaoProntoComVencedor(Item item, Usuario criador, Usuario vencedor)
    {
        LocalDateTime dataHoraInicio = LocalDateTime.now();
        LocalDateTime dataHoraFim = LocalDateTime.now().plusDays(1);

        Leilao leilao = new Leilao();

        leilao.setId(1L);
        leilao.setDataInicio(dataHoraInicio);
        leilao.setDataFim(dataHoraFim);
        leilao.setStatusLeilao(StatusLeilao.ABERTO);
        leilao.setItem(item);
        leilao.setCriador(criador);
        leilao.setVencedor(vencedor);

        return leilao;
    }


    public Leilao criarLeilaoPersonalizado(Long id, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim,
                                           StatusLeilao statusLeilao, Item item, Usuario criador)
    {
        Leilao leilao = new Leilao();
        leilao.setId(id);
        leilao.setDataInicio(dataHoraInicio);
        leilao.setDataFim(dataHoraFim);
        leilao.setStatusLeilao(statusLeilao);
        leilao.setItem(item);
        leilao.setCriador(criador);
        return leilao;
    }

    public Leilao criarLeilaoPersonalizadoComVencedor(Long id, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim,
                                           StatusLeilao statusLeilao, Item item, Usuario criador, Usuario vencedor)
    {
        Leilao leilao = new Leilao();
        leilao.setId(id);
        leilao.setDataInicio(dataHoraInicio);
        leilao.setDataFim(dataHoraFim);
        leilao.setStatusLeilao(statusLeilao);
        leilao.setItem(item);
        leilao.setCriador(criador);
        leilao.setVencedor(vencedor);
        return leilao;
    }
}
