package LeilaoOnlineJUnit.dto.item;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Usuario;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemResquestDTO(

        @NotBlank(message = "Nome de item obrigatório")
        String nomeItem,

        @NotBlank(message = "Descrição do item obrigatória")
        String descricaoItem,

        @NotBlank(message = "Categoria do item obrigatório")
        String categoriaItem,

        @NotNull(message = "Valor obrigatório") @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valorInicialItem,

        @Enumerated(EnumType.STRING)
        StatusItem statusItem,

        @NotNull(message = "Id de proprietário de item obrigatório")
        Long proprietarioItemId
) {
        public Item toItem(Usuario proprietario) {
            Item item = new Item();

            item.setNome(nomeItem);
            item.setDescricao(descricaoItem);
            item.setCategoria(categoriaItem);
            item.setValorInicial(valorInicialItem);
            item.setStatusItem(statusItem);
            item.setProprietario(proprietario);

            return item;
        }
}
