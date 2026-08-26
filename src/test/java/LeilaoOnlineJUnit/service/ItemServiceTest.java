package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
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
import LeilaoOnlineJUnit.infra.exception.NenhumRegistroException;
import LeilaoOnlineJUnit.repository.ItemRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
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

    // --- GET ALL ITEM ---

    @Test
    void listarTodosItensCadastrados()
    {
        Usuario proprietarioUm = UsuarioFactory.criarUsuarioPersonalizado(1L,"rafael","34257599065", StatusUsuario.ATIVO);
        Item itemUm = ItemFactory.criarItemPersonalizado(1L,"Bike","perfeito estado","transporte",
                StatusItem.EM_LEILAO ,proprietarioUm);

        Usuario proprietarioDois = UsuarioFactory.criarUsuarioPersonalizado(2L,"watson","83110569000", StatusUsuario.ATIVO);
        Item itemDois = ItemFactory.criarItemPersonalizado(1L,"Monitor","perfeito estado","periferico",
                StatusItem.DISPONIVEL ,proprietarioDois);

        when(itemRepository.findAll()).thenReturn(List.of(itemUm,itemDois));

        List<ItemResponseDTO> response = itemService.buscarTodosItems();

        assertNotNull(response);
        assertEquals(2,response.size());
        assertEquals(itemUm.getNome(),response.get(0).nomeItem());
        assertEquals(itemDois.getNome(),response.get(1).nomeItem());

        verify(itemRepository).findAll();
    }

    @Test
    void lancarExcecaoQuandoNaoRetornarNenhumRegistro()
    {
        when(itemRepository.findAll()).thenReturn(List.of());

        NenhumRegistroException exception = assertThrows(NenhumRegistroException.class,()->itemService.buscarTodosItems());
        assertEquals("Nenhum registro cadastrado", exception.getMessage());

        verify(itemRepository).findAll();
    }

    // --- GET BY ID ---

    @Test
    void retornarItemPorId()
    {
        Usuario proprietario =  UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemResponseDTO response = itemService.buscarItem(item.getId());
        validarDadosItem(item,response);

        verify(itemRepository).findById(item.getId());
    }

    @Test
    void retornarExcecaoQuandoIdDeItemNaoEncontrado()
    {
        Long id = 111L;

        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        IdNaoEncontradoException exception = assertThrows(IdNaoEncontradoException.class,()->itemService.buscarItem(id));
        assertEquals("Id de item não encontrado",exception.getMessage());

        verify(itemRepository).findById(id);
    }

    // --- GET BY CATEGORIA ---

    @Test
    void buscarItemPorCategoria()
    {
        Usuario proprietario =  UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findByCategoria(item.getCategoria())).thenReturn(List.of(item));

        List<ItemResponseDTO> responseList = itemService.buscarPorCategoria(item.getCategoria());
        assertNotNull(responseList);

        validarDadosItem(item,responseList.getFirst());

        verify(itemRepository).findByCategoria(item.getCategoria());
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
