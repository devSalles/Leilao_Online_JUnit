package LeilaoOnlineJUnit.dto.lance;

import LeilaoOnlineJUnit.entity.Lance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LanceResponseDTO(
        Long id,
        BigDecimal valor,
        LocalDateTime dataHora,
        Long idUsuario,
        Long idLeilao
) {
    public static LanceResponseDTO fromLance(Lance lance) {
        return new LanceResponseDTO(lance.getId(), lance.getValor(), lance.getDataHora(),
                lance.getUsuario().getId(), lance.getLeilao().getId());
    }
}
