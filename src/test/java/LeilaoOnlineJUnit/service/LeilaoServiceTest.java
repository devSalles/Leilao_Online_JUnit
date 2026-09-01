package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.ItemFactory;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.ItemEmLeilaoException;
import LeilaoOnlineJUnit.infra.exception.ItemVendidoException;
import LeilaoOnlineJUnit.infra.exception.UsuarioBloqueadoException;
import LeilaoOnlineJUnit.infra.exception.UsuarioNaoProprietarioException;
import LeilaoOnlineJUnit.repository.LanceRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeilaoServiceTest {

    @Mock
    LeilaoRepository leilaoRepository;

    @Mock
    LanceRepository lanceRepository;

    @Mock
    ItemService itemService;

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    LeilaoService leilaoService;

    @Test
    void agendarLeilao()
    {
        //Arrange
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(usuario);

        LocalDateTime dataInicio = LocalDateTime.now().plusDays(1);
        LocalDateTime dataFim = LocalDateTime.now().plusDays(2);

        when(usuarioService.buscarIdUsuario(usuario.getId())).thenReturn(usuario);
        when(itemService.buscarID(item.getId())).thenReturn(item);

        LeilaoRequestDTO leilaoRequestDTO = new LeilaoRequestDTO(dataInicio, dataFim, item.getId(), usuario.getId());

        //Act
        LeilaoResponseDTO response = leilaoService.agendarLeilao(leilaoRequestDTO);

        //Assert
        ArgumentCaptor<Leilao> captor = ArgumentCaptor.forClass(Leilao.class);

        verify(leilaoRepository).save(captor.capture());
        verify(usuarioService).buscarIdUsuario(usuario.getId());
        verify(itemService).buscarID(item.getId());

        Leilao leilao = captor.getValue();

        validarDaddosLeilao(leilao,response);
    }

    @Test
    void deveLancarExcecaoQuandoCriadorEstiverBloqueado() {

        // Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo", "34257599065", StatusUsuario.BLOQUEADO);

        Item item = ItemFactory.criarItemPersonalizado(1L, "Bicicleta", "Excelente estado", "Veículos", StatusItem.DISPONIVEL, criador);

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                item.getId(), criador.getId());

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);

        // Act + Assert
        UsuarioBloqueadoException exception = assertThrows(UsuarioBloqueadoException.class, () -> leilaoService.agendarLeilao(request));

        assertEquals("usuario bloqueado não pode fazer agendamento de leilão", exception.getMessage());

        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
    }

    @Test
    void deveLancarExcecaoQuandoCriadorNaoForProprietarioDoItem() {

        // Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPersonalizado(1L, "Proprietario",
                "34257599065",
                StatusUsuario.ATIVO);

        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(2L, "Criador", "12345678909", StatusUsuario.ATIVO);

        Item item = ItemFactory.criarItemPersonalizado(1L, "Bicicleta", "Excelente estado", "Veículos", StatusItem.DISPONIVEL, proprietario);

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item.getId(), criador.getId());

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);

        // Act + Assert
        assertThrows(UsuarioNaoProprietarioException.class, () -> leilaoService.agendarLeilao(request));

        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
    }

    @Test
    void deveLancarExcecaoQuandoItemEstiverEmLeilao() {

        // Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo",
                "34257599065", StatusUsuario.ATIVO);

        Item item = ItemFactory.criarItemPersonalizado(1L, "Bicicleta", "Excelente estado",
                "Veículos", StatusItem.EM_LEILAO, criador);

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                item.getId(), criador.getId());

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);

        // Act + Assert
        ItemEmLeilaoException exception = assertThrows(ItemEmLeilaoException.class, () -> leilaoService.agendarLeilao(request));

        assertEquals("Item em leilão não pode ser vínculado", exception.getMessage());

        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
    }

    @Test
    void deveLancarExcecaoQuandoItemEstiverVendido()
    {
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo",
                "34257599065", StatusUsuario.ATIVO);

        Item item = ItemFactory.criarItemPersonalizado(1L, "Bicicleta", "Excelente estado",
                "Veículos", StatusItem.VENDIDO, criador);

        LeilaoRequestDTO resquest = new LeilaoRequestDTO(LocalDateTime.now().plusDays(1),LocalDateTime.now().plusDays(2),
                criador.getId(),item.getId());

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);
        when(itemService.buscarID(item.getId())).thenReturn(item);

        ItemVendidoException exception = assertThrows(ItemVendidoException.class,()-> leilaoService.agendarLeilao(resquest));
        assertEquals("Item vendido não pode ser vínculado", exception.getMessage());

        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
    }

    // --- METODO AUXILIAR ---

    public void validarDaddosLeilao(Leilao leilao, LeilaoResponseDTO leilaoResponseDTO)
    {
        assertAll(
                ()-> assertNotNull(leilaoResponseDTO),
                ()-> assertEquals(leilao.getId(),leilaoResponseDTO.id()),
                ()-> assertEquals(leilao.getDataInicio(),leilaoResponseDTO.dataInicio()),
                ()-> assertEquals(leilao.getDataFim(),leilaoResponseDTO.dataFim()),
                ()-> assertEquals(leilao.getStatusLeilao(), leilaoResponseDTO.statusLeilao()),
                ()-> assertEquals(leilao.getCriador().getId(), leilaoResponseDTO.idCriador()),
                ()-> assertEquals(leilao.getItem().getId(),leilaoResponseDTO.idItem())
        );
    }
}
