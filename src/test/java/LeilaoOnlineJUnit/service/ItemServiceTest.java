package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.dto.item.ItemResponseDTO;
import LeilaoOnlineJUnit.dto.item.ItemResquestDTO;
import LeilaoOnlineJUnit.dto.item.ItemUpdateRequestDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.repository.ItemRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    UsuarioService usuarioService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    LeilaoRepository leilaoRepository;

    @InjectMocks
    ItemService itemService;

    // --- POST ITEM ---

    @Test
    void registrarItens()
    {
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();

        when(usuarioService.buscarIdUsuario(proprietario.getId())).thenReturn(proprietario);

        ItemResquestDTO itemRequest = new ItemResquestDTO("Bicicleta","Excelente estado",
                "veíclos",new BigDecimal("2500.00"), proprietario.getId());

        ItemResponseDTO itemResponseDTO = itemService.salvarItem(itemRequest);
        assertNotNull(itemResponseDTO);

        verify(usuarioService).buscarIdUsuario(proprietario.getId());
        verify(itemRepository).save(any(Item.class));

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);

        verify(itemRepository).save(captor.capture());

        Item capturedItem = captor.getValue();

        validarDadosItem(capturedItem, itemResponseDTO);
    }


    @Test
    void lancarExcecaoQuandoPropritarioNaoExistir()
    {
        Long idProprietario = 100L;

        when(usuarioService.buscarIdUsuario(idProprietario)).thenThrow(new IdNaoEncontradoException("Usuário não encontrado"));

        ItemResquestDTO itemRequest = new ItemResquestDTO("Bicicleta","Excelente estado",
                "veíclos",new BigDecimal("2500.00"), idProprietario);

        IdNaoEncontradoException exception = assertThrows(IdNaoEncontradoException.class, () ->  itemService.salvarItem(itemRequest));
        assertEquals("Usuário não encontrado",exception.getMessage());

        verify(usuarioService).buscarIdUsuario(idProprietario);
        verify(itemRepository,never()).save(any(Item.class));
    }

    // --- METODO AUXILIAR ---

    private void validarDadosItem(Item item, ItemResponseDTO itemResponseDTO)
    {
        assertAll(
                ()->assertNotNull(itemResponseDTO),
                ()-> assertEquals(item.getId(),itemResponseDTO.id()),
                ()->assertEquals(item.getNome(),itemResponseDTO.nomeItem()),
                ()->assertEquals(item.getDescricao(),itemResponseDTO.descricaoItem()),
                ()->assertEquals(item.getValorInicial(),itemResponseDTO.valorInicialItem()),
                ()->assertEquals(item.getStatusItem(),itemResponseDTO.statusItem())
        );
    }
}
