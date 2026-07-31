package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.dto.item.ItemResponseDTO;
import LeilaoOnlineJUnit.dto.item.ItemResquestDTO;
import LeilaoOnlineJUnit.dto.item.ItemUpdateRequestDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.NenhumRegistroException;
import LeilaoOnlineJUnit.infra.exception.item.ItemEmLeilaoException;
import LeilaoOnlineJUnit.infra.exception.item.ItemVendidoException;
import LeilaoOnlineJUnit.infra.exception.item.ItemVinculadoAoLeilaoException;
import LeilaoOnlineJUnit.repository.ItemRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UsuarioService usuarioService;
    private final ItemRepository itemRepository;
    private final LeilaoRepository leilaoRepository;

    @Transactional
    public ItemResponseDTO salvarItem(ItemResquestDTO itemResquestDTO)
    {
        Usuario proprietario = usuarioService.buscarIdUsuario(itemResquestDTO.proprietarioItemId());

        Item itemSalvo = itemResquestDTO.toItem(proprietario);
        itemRepository.save(itemSalvo);

        return ItemResponseDTO.fromItem(itemSalvo);
    }

    @Transactional
    public ItemResponseDTO atualizarItem(Long id, ItemUpdateRequestDTO itemUpdateRequestDTO)
    {
        Item itemID = itemRepository.findById(id).orElseThrow(()-> new IdNaoEncontradoException("Id de item não encontrado"));

        validarItemEditavel(itemID);

        Item itemAtualizado = itemUpdateRequestDTO.updateItem(itemID);
        itemRepository.save(itemAtualizado);
        return ItemResponseDTO.fromItem(itemAtualizado);
    }

    public List<ItemResponseDTO> buscarTodosItems()
    {
        List<Item> itens = itemRepository.findAll();
        if(itens.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro cadastrado");
        }
        return itens.stream().map(ItemResponseDTO::fromItem).toList();
    }

    public ItemResponseDTO buscarItem(Long id)
    {
        Item itemID = buscarID(id);
        return ItemResponseDTO.fromItem(itemID);
    }

    public List<ItemResponseDTO> buscarPorCategoria(String categoria)
    {
        List<Item> itemCategoria = itemRepository.findByCategoria(categoria);
        if (itemCategoria.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro de categoria encontrado");
        }
        return itemCategoria.stream().map(ItemResponseDTO::fromItem).toList();
    }

    public List<ItemResponseDTO> buscarItemPorStatus(StatusItem statusItem)
    {
        List<Item> itemStatus = itemRepository.findByStatusItem(statusItem);
        if (itemStatus.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro de status encontrado");
        }
        return itemStatus.stream().map(ItemResponseDTO::fromItem).toList();
    }

    public List<ItemResponseDTO> buscarItemPorProprietario(Long proprietarioId)
    {
        List<Item> itemProprietario = itemRepository.findByProprietarioId(proprietarioId);
        if(itemProprietario.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro de item de proprietário encontrado");
        }
        return itemProprietario.stream().map(ItemResponseDTO::fromItem).toList();
    }

    public List<ItemResponseDTO> buscarPorNome(String nome)
    {
        List<Item> itemNome = itemRepository.findByNome(nome);
        if (itemNome.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro de categoria encontrado");
        }
        return itemNome.stream().map(ItemResponseDTO::fromItem).toList();
    }

     public void removerItem(Long idItem)
     {
        Item itemID = buscarID(idItem);

        boolean possuiLeilaoVinculado = leilaoRepository.existsByItemId(idItem);
        if(possuiLeilaoVinculado)
        {
            throw new  ItemVinculadoAoLeilaoException();
        }

        if(itemID.getStatusItem() != StatusItem.DISPONIVEL)
        {
            throw new ItemVinculadoAoLeilaoException();
        }

        itemRepository.delete(itemID);
     }

    // --- Metodo Auxiliar ---

    public Item buscarID(Long id)
    {
        return itemRepository.findById(id).orElseThrow(()->new IdNaoEncontradoException("Id de item não encontrado"));
    }

    private void validarItemEditavel(Item item)
    {
        if(item.getStatusItem() == StatusItem.EM_LEILAO)
        {
            throw new ItemEmLeilaoException();
        }

        if(item.getStatusItem() == StatusItem.VENDIDO)
        {
            throw new ItemVendidoException();
        }
    }
}
