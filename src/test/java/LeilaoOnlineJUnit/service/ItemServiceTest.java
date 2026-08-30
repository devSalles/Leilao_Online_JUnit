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
import LeilaoOnlineJUnit.infra.exception.*;
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
        // Arrange
        Usuario proprietarioUm = UsuarioFactory.criarUsuarioPersonalizado(1L, "rafael", "34257599065", StatusUsuario.ATIVO);

        Item itemUm = ItemFactory.criarItemPersonalizado(1L, "Bike", "perfeito estado", "transporte", StatusItem.EM_LEILAO, proprietarioUm);

        Usuario proprietarioDois = UsuarioFactory.criarUsuarioPersonalizado(2L, "watson", "83110569000", StatusUsuario.ATIVO);

        Item itemDois = ItemFactory.criarItemPersonalizado(1L, "Monitor", "perfeito estado", "periferico", StatusItem.DISPONIVEL, proprietarioDois);

        when(itemRepository.findAll()).thenReturn(List.of(itemUm, itemDois));

        // Act
        List<ItemResponseDTO> response = itemService.buscarTodosItems();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(itemUm.getNome(), response.get(0).nomeItem());
        assertEquals(itemDois.getNome(), response.get(1).nomeItem());

        verify(itemRepository).findAll();
    }

    @Test
    void lancarExcecaoQuandoNaoRetornarNenhumRegistro()
    {
        // Arrange
        when(itemRepository.findAll()).thenReturn(List.of());

        // Act
        NenhumRegistroException exception = assertThrows(NenhumRegistroException.class, () -> itemService.buscarTodosItems());

        // Assert
        assertEquals("Nenhum registro cadastrado", exception.getMessage());

        verify(itemRepository).findAll();
    }


    // --- GET BY ID ---

    @Test
    void retornarItemPorId()
    {
        // Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        // Act
        ItemResponseDTO response = itemService.buscarItem(item.getId());

        // Assert
        validarDadosItem(item, response);

        verify(itemRepository).findById(item.getId());
    }

    @Test
    void retornarExcecaoQuandoIdDeItemNaoEncontrado()
    {
        // Arrange
        Long id = 111L;

        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        IdNaoEncontradoException exception = assertThrows(IdNaoEncontradoException.class, () -> itemService.buscarItem(id));

        // Assert
        assertEquals("Id de item não encontrado", exception.getMessage());

        verify(itemRepository).findById(id);
    }


    // --- GET BY CATEGORIA ---

    @Test
    void buscarItemPorCategoria()
    {
        // Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findByCategoria(item.getCategoria())).thenReturn(List.of(item));

        // Act
        List<ItemResponseDTO> responseList = itemService.buscarPorCategoria(item.getCategoria());

        // Assert
        assertNotNull(responseList);

        validarDadosItem(item, responseList.getFirst());

        verify(itemRepository).findByCategoria(item.getCategoria());
    }

    @Test
    void excecaoQuandoNenhumRegistroEncontrado()
    {
        // Arrange
        StatusItem statusItem = StatusItem.VENDIDO;

        when(itemRepository.findByStatusItem(statusItem)).thenReturn(List.of());

        // Act
        NenhumRegistroException exception = assertThrows(NenhumRegistroException.class, () -> itemService.buscarItemPorStatus(statusItem));

        // Assert
        assertEquals("Nenhum registro de status encontrado", exception.getMessage());

        verify(itemRepository).findByStatusItem(statusItem);
    }

    // --- GET BY STATUS ---

    @Test
    void buscarItemPorStatus()
    {
        // Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findByStatusItem(item.getStatusItem())).thenReturn(List.of(item));

        // Act
        List<ItemResponseDTO> responseList = itemService.buscarItemPorStatus(item.getStatusItem());

        // Assert
        assertNotNull(responseList);

        validarDadosItem(item, responseList.getFirst());

        verify(itemRepository).findByStatusItem(item.getStatusItem());
    }

    @Test
    void excecaoQuandoNenhumItemEncontradoPorStatus()
    {
        // Arrange
        StatusItem statusItem = StatusItem.VENDIDO;

        when(itemRepository.findByStatusItem(statusItem)).thenReturn(List.of());

        // Act
        NenhumRegistroException exception = assertThrows(NenhumRegistroException.class, () -> itemService.buscarItemPorStatus(statusItem));

        // Assert
        assertEquals("Nenhum registro de status encontrado", exception.getMessage());

        verify(itemRepository).findByStatusItem(statusItem);
    }

    // --- GET BY PROPRIETARIO ---

    @Test
    void buscarItemPorProprietario()
    {
        // Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findByProprietarioId(proprietario.getId())).thenReturn(List.of(item));

        // Act
        List<ItemResponseDTO> responseList = itemService.buscarItemPorProprietario(proprietario.getId());

        // Assert
        assertNotNull(responseList);

        validarDadosItem(item, responseList.getFirst());

        verify(itemRepository).findByProprietarioId(proprietario.getId());
    }

    @Test
    void excecaoQuandoNenhumItemEncontradoPorProprietario()
    {
        // Arrange
        Long proprietarioId = 1L;

        when(itemRepository.findByProprietarioId(proprietarioId)).thenReturn(List.of());

        // Act
        NenhumRegistroException exception = assertThrows(NenhumRegistroException.class, () -> itemService.buscarItemPorProprietario(proprietarioId));

        // Assert
        assertEquals("Nenhum registro de item de proprietário encontrado", exception.getMessage());

        verify(itemRepository).findByProprietarioId(proprietarioId);
    }

    // --- GET BY NOME ---

    @Test
    void buscarPorNome()
    {
        // Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findByNome(item.getNome())).thenReturn(List.of(item));

        // Act
        List<ItemResponseDTO> responseList = itemService.buscarPorNome(item.getNome());

        // Assert
        assertNotNull(responseList);

        validarDadosItem(item, responseList.getFirst());

        verify(itemRepository).findByNome(item.getNome());
    }

    @Test
    void excecaoQuandoNenhumItemEncontradoPorNome()
    {
        // Arrange
        String nome = "Bicicleta";

        when(itemRepository.findByNome(nome)).thenReturn(List.of());

        // Act
        NenhumRegistroException exception = assertThrows(NenhumRegistroException.class, () -> itemService.buscarPorNome(nome));

        // Assert
        assertEquals("Nenhum registro de categoria encontrado", exception.getMessage());

        verify(itemRepository).findByNome(nome);
    }

    // --- DELETE BY ID ---

    @Test
    void deletarItemPorId()
    {
        //Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        //Act
        itemService.removerItem(item.getId());

        //Assert
        verify(itemRepository).delete(item);
        verify(itemRepository).findById(item.getId());
        verify(leilaoRepository).existsByItemId(item.getId());

    }

    @Test
    void lancarExcecaoQuandoNenhumItemEncontradoPorId()
    {
        //Arrange
        Long idItem = 111L;

        when(itemRepository.findById(idItem)).thenReturn(Optional.empty());

        //Act
        IdNaoEncontradoException exception = assertThrows(IdNaoEncontradoException.class,()->itemService.removerItem(idItem));

        //Assert
        assertEquals("Id de item não encontrado",exception.getMessage());

        verify(itemRepository).findById(idItem);
        verifyNoInteractions(leilaoRepository);
    }

    @Test
    void lancarExcecaoQuandoItemPossuirLeilaoVinculado()
    {
        //Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(leilaoRepository.existsByItemId(item.getId())).thenReturn(true);

        //Act
        ItemVinculadoAoLeilaoException exception = assertThrows(ItemVinculadoAoLeilaoException.class,()->itemService.removerItem(item.getId()));

        //Assert
        assertEquals("Item vínculado ao leilão não pode ser excluído", exception.getMessage());

        verify(itemRepository).findById(item.getId());
        verify(leilaoRepository).existsByItemId(item.getId());
        verify(itemRepository, never()).delete(any(Item.class));
    }

    @Test
    void lancarExcecaoQuandoStatusDeitemDiferenteDeDisponivel()
    {
        //Arange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPersonalizado(1L,"QCY T43","Excelente estado","Fone",StatusItem.EM_LEILAO, proprietario);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        //Act
        ItemVinculadoAoLeilaoException exception = assertThrows(ItemVinculadoAoLeilaoException.class,()->itemService.removerItem(item.getId()));

        //Assert
        assertEquals("Item vínculado ao leilão não pode ser excluído",exception.getMessage());

        verify(itemRepository).findById(item.getId());
        verify(leilaoRepository).existsByItemId(item.getId());
        verify(itemRepository, never()).delete(any(Item.class));

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
