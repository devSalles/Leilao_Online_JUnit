package LeilaoOnlineJUnit.dto.lance;

import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LanceRequestDTO(

    @NotNull(message = "Valor de lance obrigatório")
    @DecimalMin(value = "0.01",message = "Valor deve ser maior que zero")
    @Digits(integer = 10,fraction = 2,message = "O valor deve ter no máximo 10 digitos inteiros e 2 decimais")
    BigDecimal valorLance,

    @NotNull(message = "O ID do usuário é obrigatório")
    Long idUsuario,

    @NotNull(message = "O ID leilão é obrigatório")
    Long idLeilao
) {

    public Lance toLance(Usuario usuario, Leilao leilao)
    {
        Lance lance = new Lance();

        lance.setValor(valorLance);
        lance.setDataHora(LocalDateTime.now());
        lance.setUsuario(usuario);
        lance.setLeilao(leilao);

        return lance;
    }
}
