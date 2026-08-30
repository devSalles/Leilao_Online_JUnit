package LeilaoOnlineJUnit.factory;

import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@UtilityClass
public class LanceFactory {

    public Lance criarLancePronto(Usuario usuario, Leilao leilao)
    {
        Lance lance = new Lance();

        lance.setId(1L);
        lance.setDataHora(LocalDateTime.now());
        lance.setValor(new BigDecimal("10000.00"));
        lance.setLeilao(leilao);
        lance.setUsuario(usuario);

        return lance;
    }

    public Lance criarLancePersonalizado(Long id, BigDecimal valor,LocalDateTime dataHora, Leilao leilao, Usuario usuario)
    {
        Lance lance = new Lance();

        lance.setId(id);
        lance.setValor(valor);
        lance.setDataHora(dataHora);
        lance.setUsuario(usuario);
        lance.setLeilao(leilao);

        return lance;
    }
}
