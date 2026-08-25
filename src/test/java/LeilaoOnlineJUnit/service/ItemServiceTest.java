package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.dto.item.ItemResponseDTO;
import LeilaoOnlineJUnit.dto.item.ItemResquestDTO;
import LeilaoOnlineJUnit.dto.item.ItemUpdateRequestDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.ItemFactory;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.ItemEmLeilaoException;
import LeilaoOnlineJUnit.infra.exception.ItemVendidoException;
import LeilaoOnlineJUnit.repository.ItemRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

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
        // Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();

        when(usuarioService.buscarIdUsuario(proprietario.getId())).thenReturn(proprietario);

        ItemResquestDTO itemRequest = new ItemResquestDTO("Bicicleta","Excelente estado",
                "veíclos",new BigDecimal("2500.00"), proprietario.getId());

        //Act
        ItemResponseDTO itemResponseDTO = itemService.salvarItem(itemRequest);

        //Assert
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
        //Arrange
        Long idProprietario = 100L;

        when(usuarioService.buscarIdUsuario(idProprietario)).thenThrow(new IdNaoEncontradoException("Usuário não encontrado"));

        ItemResquestDTO itemRequest = new ItemResquestDTO("Bicicleta","Excelente estado",
                "veíclos",new BigDecimal("2500.00"), idProprietario);

        //Act
        IdNaoEncontradoException exception = assertThrows(IdNaoEncontradoException.class, () ->  itemService.salvarItem(itemRequest));

        //Assert
        assertEquals("Usuário não encontrado",exception.getMessage());

        verify(usuarioService).buscarIdUsuario(idProprietario);
        verify(itemRepository,never()).save(any(Item.class));
    }

    // --- PUT ITEM ---

    @Test
    void atualizarItem()
    {
        //Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemUpdateRequestDTO itemUpdateDTO = new ItemUpdateRequestDTO("Carro","perfeito estado",
                "Veículo",new BigDecimal("20.00"));

        //Act
        ItemResponseDTO responde = itemService.atualizarItem(item.getId(),itemUpdateDTO);

        //Assert
        validarDadosItem(item,responde);

        verify(itemRepository).save(any(Item.class));
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void lancarExcecaoCasoItemEstiverEmLeilao()
    {
        //Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPersonalizado(1L,"Iphone","perfeito estado","telefone"
                ,StatusItem.EM_LEILAO ,proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemUpdateRequestDTO itemUpdateDTO = new ItemUpdateRequestDTO("Carro","perfeito estado",
                "Veículo",new BigDecimal("20.00"));

        //Act
        ItemEmLeilaoException exception = assertThrows(ItemEmLeilaoException.class,()->itemService.atualizarItem(item.getId(), itemUpdateDTO));

        //Assert
        assertEquals("Um item em leilão não pode ser editado", exception.getMessage());

        verify(itemRepository,never()).save(any(Item.class));
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void lancarExcecaoQuandoTentarAtualizarItemVendido()
    {
        //Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPersonalizado(1L,"Iphone","perfeito estado","telefone"
                ,StatusItem.VENDIDO ,proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemUpdateRequestDTO itemUpdateDTO = new ItemUpdateRequestDTO("Carro","perfeito estado",
                "Veículo",new BigDecimal("20.00"));

        //Act
        ItemVendidoException exception = assertThrows(ItemVendidoException.class,()->itemService.atualizarItem(item.getId(), itemUpdateDTO));

        //Assert
        assertEquals("Item vendido não pode ser editado", exception.getMessage());

        verify(itemRepository,never()).save(any(Item.class));
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void lancarExcecaoQuandoIdProprietarioInexistente()
    {

        //Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());


        ItemUpdateRequestDTO itemUpdateDTO = new ItemUpdateRequestDTO("Carro","perfeito estado",
                "Veículo",new BigDecimal("20.00"));

        //Act
        IdNaoEncontradoException exception = assertThrows(IdNaoEncontradoException.class,()->itemService.atualizarItem(item.getId(), itemUpdateDTO));

        //Assert
        assertEquals("Id de item não encontrado", exception.getMessage());

        verify(itemRepository,never()).save(any(Item.class));
        verify(itemRepository).findById(item.getId());
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
