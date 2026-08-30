package LeilaoOnlineJUnit.factory;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Usuario;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemFactory {

    public static Item criarItemPronto(Usuario proprietario)
    {
        Item item = new Item();

        item.setId(1L);
        item.setNome("Bicicleta");
        item.setCategoria("Transporte");
        item.setDescricao("Em perfeito estado");
        item.setStatusItem(StatusItem.DISPONIVEL);
        item.setProprietario(proprietario);

        return item;
    }

    public static Item criarItemPersonalizado(Long id, String nome, String descricao, String categoria,
                                       StatusItem statusItem, Usuario proprietario)
    {
        Item item = new Item();

        item.setId(id);
        item.setNome(nome);
        item.setDescricao(descricao);
        item.setCategoria(categoria);
        item.setStatusItem(statusItem);
        item.setProprietario(proprietario);

        return item;
    }
}
