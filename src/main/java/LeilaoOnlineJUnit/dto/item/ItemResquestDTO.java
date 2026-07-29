package LeilaoOnlineJUnit.dto.item;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Usuario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemResquestDTO(

        @NotBlank(message = "Nome de item obrigatório")
        String nomeItem,

        @NotBlank(message = "Descrição do item obrigatória")
        String descricaoItem,

        @NotBlank(message = "Categoria do item obrigatório")
        String categoriaItem,

        @NotNull(message = "Valor obrigatório") @DecimalMin(value = "0.01")
        BigDecimal valorInicialItem,

        @NotNull(message = "Id de proprietário de item obrigatório")
        Long proprietarioItemId
) {
        public Item toItem(Usuario proprietario) {
            Item item = new Item();

            item.setNome(nomeItem);
            item.setDescricao(descricaoItem);
            item.setCategoria(categoriaItem);
            item.setValorInicial(valorInicialItem);
            item.setStatusItem(StatusItem.DISPONIVEL);
            item.setProprietario(proprietario);

            return item;
        }
}
