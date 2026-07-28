package LeilaoOnlineJUnit.dto.item;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.entity.Item;

import java.math.BigDecimal;

public record ItemResponseDTO(
        Long id,
        String nomeItem,
        String descricaoItem,
        String categoriaItem,
        BigDecimal valorInicialItem,
        StatusItem statusItem,
        Long proprietarioItemId
) {
    public ItemResponseDTO fromItem(Item item)
    {
        return  new ItemResponseDTO(item.getId(),item.getNome(),item.getDescricao(),item.getCategoria(),item.getValorInicial()
        ,item.getStatusItem(),item.getProprietario().getId());
    }
}
