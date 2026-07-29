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
import LeilaoOnlineJUnit.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final UsuarioService usuarioService;
    private final ItemRepository itemRepository;

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

        if(itemID.getStatusItem().equals(StatusItem.EM_LEILAO))
        {
            throw new ItemEmLeilaoException();
        }

        Item itemAtualizado = itemUpdateRequestDTO.updateItem(itemID);
        itemRepository.save(itemAtualizado);
        return ItemResponseDTO.fromItem(itemAtualizado);
    }

   
}
