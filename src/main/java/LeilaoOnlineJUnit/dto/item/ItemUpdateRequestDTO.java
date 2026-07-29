package LeilaoOnlineJUnit.dto.item;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.entity.Item;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemUpdateRequestDTO(

        @NotBlank(message = "Nome de item obrigatório")
        String nomeItem,

        @NotBlank(message = "Descrição do item obrigatória")
        String descricaoItem,

        @NotBlank(message = "Categoria do item obrigatório")
        String categoriaItem,

        @NotNull(message = "Valor obrigatório") @DecimalMin(value = "0.01")
        BigDecimal valorInicialItem
) {
    public Item updateItem(Item item) {

        item.setNome(nomeItem);
        item.setDescricao(descricaoItem);
        item.setCategoria(categoriaItem);
        item.setValorInicial(valorInicialItem);

        return item;
    }
}

