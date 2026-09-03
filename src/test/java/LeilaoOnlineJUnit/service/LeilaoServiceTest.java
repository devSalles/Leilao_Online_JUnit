package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusItem;
import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.ItemFactory;
import LeilaoOnlineJUnit.factory.LeilaoFactory;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.*;
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
import static org.mockito.Mockito.*;

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

    // --- POST AGENDAR LEILAO ---

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

    @Test
    void deveLancarExcecaoQuandoDataInicioForPosteriorADataFim() {

        // Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo",
                "34257599065", StatusUsuario.ATIVO);

        Item item = ItemFactory.criarItemPersonalizado(1L, "Bicicleta", "Excelente estado",
                "Veículos", StatusItem.DISPONIVEL, criador);

        LocalDateTime dataInicio = LocalDateTime.now().plusDays(3);
        LocalDateTime dataFim = LocalDateTime.now().plusDays(2);

        LeilaoRequestDTO request = new LeilaoRequestDTO(dataInicio, dataFim, item.getId(), criador.getId());

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);

        // Act + Assert
        assertThrows(DataIncorretaException.class, () -> leilaoService.agendarLeilao(request));

        // Assert - verificando as chamadas
        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
    }

    @Test
    void deveLancarExcecaoQuandoDataInicioNaoForFutura() {

        // Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo",
                "34257599065", StatusUsuario.ATIVO);

        Item item = ItemFactory.criarItemPersonalizado(1L, "Bicicleta", "Excelente estado",
                "Veículos", StatusItem.DISPONIVEL, criador);

        LocalDateTime dataInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime dataFim = LocalDateTime.now().plusDays(2);

        LeilaoRequestDTO request = new LeilaoRequestDTO(dataInicio, dataFim, item.getId(), criador.getId());

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);

        // Act + Assert
        DataIncorretaException exception = assertThrows(DataIncorretaException.class, () -> leilaoService.agendarLeilao(request));

        assertEquals("A data de início está incorreta", exception.getMessage());

        // Assert - verificando as chamadas
        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
    }

    @Test
    void deveLancarExcecaoQuandoDataFimNaoForPosteriorADataInicio() {

        // Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo",
                "34257599065", StatusUsuario.ATIVO);

        Item item = ItemFactory.criarItemPersonalizado(1L, "Bicicleta", "Excelente estado",
                "Veículos", StatusItem.DISPONIVEL, criador);

        LocalDateTime dataInicio = LocalDateTime.now().plusDays(2);
        LocalDateTime dataFim = dataInicio;

        LeilaoRequestDTO request = new LeilaoRequestDTO(dataInicio, dataFim, item.getId(), criador.getId());

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);

        // Act + Assert
        DataIncorretaException exception = assertThrows(DataIncorretaException.class, () -> leilaoService.agendarLeilao(request));

        assertEquals("A data de encerramento está incorreta", exception.getMessage());

        // Assert - verificando as chamadas
        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
    }

    // --- PUT LEILÃO ---

    @Test
    void atualizarLeilao()
    {
        //Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo",
                "28784851066", StatusUsuario.ATIVO);
        Item item = ItemFactory.criarItemPersonalizado(1L, "CERATO"," excelente estado",
                "veículos", StatusItem.DISPONIVEL,criador);

        Leilao leilao = LeilaoFactory.criarLeilaoPersonalizado(1L,LocalDateTime.now().plusDays(1),LocalDateTime.now().plusDays(1)
                ,StatusLeilao.AGENDADO,item,criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);
        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);
        when(leilaoRepository.findById(leilao.getId())).thenReturn(Optional.of(leilao));

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4),
                item.getId(), criador.getId());

        //Act
        LeilaoResponseDTO response = leilaoService.atualizarLeilao(leilao.getId(),request);

        //Assert
        validarDaddosLeilao(leilao,response);
        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
        verify(leilaoRepository).save(any(Leilao.class));
    }

    @Test
    void LancarExcecaoQuandoStatusDeLeilaoForDiferenteDeAgendado()
    {
        //Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo",
                "28784851066", StatusUsuario.ATIVO);
        Item item = ItemFactory.criarItemPersonalizado(1L, "CERATO"," excelente estado",
                "veículos", StatusItem.DISPONIVEL,criador);

        Leilao leilao = LeilaoFactory.criarLeilaoPersonalizado(1L,LocalDateTime.now().plusDays(1),LocalDateTime.now().plusDays(1)
                ,StatusLeilao.ABERTO,item,criador);

        when(leilaoRepository.findById(leilao.getId())).thenReturn(Optional.of(leilao));
        lenient().when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);
        lenient().when(itemService.buscarID(item.getId())).thenReturn(item);

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4),
                item.getId(), criador.getId());

        //Act
        StatusDeLeilaoIncorretoException exception = assertThrows(StatusDeLeilaoIncorretoException.class,
                ()-> leilaoService.atualizarLeilao(leilao.getId(),request));

        //Assert
        assertEquals("Apenas leilões com status de AGENDADO podem ser atualizados",exception.getMessage());

        verify(leilaoRepository).findById(leilao.getId());
        verify(leilaoRepository,never()).save(any(Leilao.class));
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
