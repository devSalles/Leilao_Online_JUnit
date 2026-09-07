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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    void agendarLeilao() {
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

        validarDaddosLeilao(leilao, response);
    }

    @ParameterizedTest
    @MethodSource("cenariosValidacaoCriadorItem")
    void lancarExcecaoAoValidarCriadorEItem(
            StatusUsuario statusUsuario,
            StatusItem statusItem,
            Long idProprietatio,
            Long idCriador,
            Class<? extends RuntimeException> exceptionClass
    )
    {
        Usuario proprietario = UsuarioFactory.criarUsuarioPersonalizado(idProprietatio,"Bernardo","73056435056",statusUsuario);
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(idCriador,"Anderson","17733878047",statusUsuario);
        Item item = ItemFactory.criarItemPersonalizado(1L,"Bicicleta","Excelente estado","transporte",statusItem,proprietario);

        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);
        when(itemService.buscarID(item.getId())).thenReturn(item);

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item.getId(), criador.getId());

        assertThrows(exceptionClass, () -> leilaoService.agendarLeilao(request));

        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
        verify(leilaoRepository,never()).save(any(Leilao.class));
    }

    @Test
    void deveLancarExcecaoQuandoDataInicioForPosteriorADataFim() {

        // Arrange
        LocalDateTime dataInicio = LocalDateTime.now().plusDays(3);
        LocalDateTime dataFim = LocalDateTime.now().plusDays(2);

        LeilaoRequestDTO request = new LeilaoRequestDTO(dataInicio, dataFim, 1L, 1L);

        // Act + Assert
        assertThrows(DataIncorretaException.class, () -> leilaoService.agendarLeilao(request));
    }

    @Test
    void deveLancarExcecaoQuandoDataInicioNaoForFutura() {

        // Arrange
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(1);

        LocalDateTime dataFim = LocalDateTime.now().plusDays(2);

        LeilaoRequestDTO request = new LeilaoRequestDTO(dataInicio, dataFim, 1L, 1L);

        // Act + Assert
        DataIncorretaException exception = assertThrows(DataIncorretaException.class, () -> leilaoService.agendarLeilao(request));

        assertEquals("A data de início está incorreta", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDataFimNaoForPosteriorADataInicio() {

        // Arrange
        LocalDateTime dataInicio = LocalDateTime.now().plusDays(2);

        LocalDateTime dataFim = dataInicio;

        LeilaoRequestDTO request = new LeilaoRequestDTO(dataInicio, dataFim, 1L, 1L);

        // Act + Assert
        DataIncorretaException exception = assertThrows(DataIncorretaException.class, () -> leilaoService.agendarLeilao(request));

        assertEquals("A data de encerramento está incorreta", exception.getMessage());
    }


    // --- PUT LEILÃO ---

    @Test
    void atualizarLeilao() {
        //Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo", "28784851066", StatusUsuario.ATIVO);
        Item item = ItemFactory.criarItemPersonalizado(1L, "CERATO", " excelente estado", "veículos", StatusItem.DISPONIVEL, criador);
        Leilao leilao = LeilaoFactory.criarLeilaoPersonalizado(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), StatusLeilao.AGENDADO, item, criador);

        when(itemService.buscarID(item.getId())).thenReturn(item);
        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);
        when(leilaoRepository.findById(leilao.getId())).thenReturn(Optional.of(leilao));

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), item.getId(), criador.getId());

        //Act
        LeilaoResponseDTO response = leilaoService.atualizarLeilao(leilao.getId(), request);

        //Assert
        validarDaddosLeilao(leilao, response);
        verify(usuarioService).buscarIdUsuario(criador.getId());
        verify(itemService).buscarID(item.getId());
        verify(leilaoRepository).save(any(Leilao.class));
    }

    @Test
    void LancarExcecaoQuandoStatusDeLeilaoForDiferenteDeAgendado() {

        // Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo", "28784851066", StatusUsuario.ATIVO);
        Item item = ItemFactory.criarItemPersonalizado(1L, "CERATO", "excelente estado", "veículos", StatusItem.DISPONIVEL, criador);
        Leilao leilao = LeilaoFactory.criarLeilaoPersonalizado(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), StatusLeilao.ABERTO, item, criador);

        when(leilaoRepository.findById(leilao.getId())).thenReturn(Optional.of(leilao));

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), item.getId(), criador.getId());

        // Act + Assert
        StatusDeLeilaoIncorretoException exception = assertThrows(StatusDeLeilaoIncorretoException.class, () -> leilaoService.atualizarLeilao(leilao.getId(), request));

        // Assert
        assertEquals("Apenas leilões com status de AGENDADO podem ser atualizados",exception.getMessage());

        verify(leilaoRepository).findById(leilao.getId());
        verify(leilaoRepository, never()).save(any(Leilao.class));
    }

    @Test
    void atualizarLeilaoDeveValidarCriadorEItem() {

        // Arrange
        Usuario criador = UsuarioFactory.criarUsuarioPersonalizado(1L, "Bernardo", "28784851066", StatusUsuario.BLOQUEADO);
        Item item = ItemFactory.criarItemPersonalizado(1L, "CERATO", "excelente estado", "veículos", StatusItem.DISPONIVEL, criador);
        Leilao leilao = LeilaoFactory.criarLeilaoPersonalizado(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), StatusLeilao.AGENDADO, item, criador);

        when(leilaoRepository.findById(leilao.getId())).thenReturn(Optional.of(leilao));
        when(itemService.buscarID(item.getId())).thenReturn(item);
        when(usuarioService.buscarIdUsuario(criador.getId())).thenReturn(criador);

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), item.getId(), criador.getId());

        // Act + Assert
        assertThrows(UsuarioBloqueadoException.class,()->leilaoService.atualizarLeilao(leilao.getId(), request));

        verify(leilaoRepository, never()).save(any(Leilao.class));
    }

    @Test
    void lancarExcecaoQuandoItemJaEstiverVinculadoAOutroLeilao() {

        //Arrange
        Usuario proprietario = UsuarioFactory.criarUsuarioPronto();
        Item itemAtual = ItemFactory.criarItemPronto(proprietario);
        Item outroItem = ItemFactory.criarItemPersonalizado(2L, "Civic", "Excelente estado", "veículos", StatusItem.DISPONIVEL, proprietario);
        Leilao leilao = LeilaoFactory.criarLeilaoPersonalizado(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3), StatusLeilao.AGENDADO, itemAtual, proprietario);

        when(leilaoRepository.findById(leilao.getId())).thenReturn(Optional.of(leilao));
        when(itemService.buscarID(outroItem.getId())).thenReturn(outroItem);
        when(usuarioService.buscarIdUsuario(proprietario.getId())).thenReturn(proprietario);
        when(leilaoRepository.existsByItemIdAndStatusLeilaoIn(outroItem.getId(), List.of(StatusLeilao.AGENDADO, StatusLeilao.ABERTO))).thenReturn(true);

        LeilaoRequestDTO request = new LeilaoRequestDTO(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), outroItem.getId(), proprietario.getId());

        // Act + Assert
        assertThrows(ItemVinculadoAoLeilaoException.class, () -> leilaoService.atualizarLeilao(leilao.getId(), request));

        // Assert
        verify(leilaoRepository).findById(leilao.getId());
        verify(itemService).buscarID(outroItem.getId());
        verify(usuarioService).buscarIdUsuario(proprietario.getId());
        verify(leilaoRepository).existsByItemIdAndStatusLeilaoIn(outroItem.getId(), List.of(StatusLeilao.AGENDADO, StatusLeilao.ABERTO));
        verify(leilaoRepository, never()).save(any(Leilao.class));
    }
    // --- METODO AUXILIAR ---

    private static Stream<Arguments> cenariosValidacaoCriadorItem()
    {
        return Stream.of(
                Arguments.of(StatusUsuario.BLOQUEADO, StatusItem.DISPONIVEL, 1L, 1L, UsuarioBloqueadoException.class),

                Arguments.of(StatusUsuario.ATIVO,StatusItem.DISPONIVEL,2L, 1L, UsuarioNaoProprietarioException.class),

                Arguments.of(StatusUsuario.ATIVO, StatusItem.EM_LEILAO, 1L,1L, ItemEmLeilaoException.class),

                Arguments.of(StatusUsuario.ATIVO,StatusItem.VENDIDO,1L,1L,ItemVendidoException.class)
        );
    }

    public void validarDaddosLeilao(Leilao leilao, LeilaoResponseDTO leilaoResponseDTO) {
        assertAll(
                () -> assertNotNull(leilaoResponseDTO),
                () -> assertEquals(leilao.getId(), leilaoResponseDTO.id()),
                () -> assertEquals(leilao.getDataInicio(), leilaoResponseDTO.dataInicio()),
                () -> assertEquals(leilao.getDataFim(), leilaoResponseDTO.dataFim()),
                () -> assertEquals(leilao.getStatusLeilao(), leilaoResponseDTO.statusLeilao()),
                () -> assertEquals(leilao.getCriador().getId(), leilaoResponseDTO.idCriador()),
                () -> assertEquals(leilao.getItem().getId(), leilaoResponseDTO.idItem())
        );
    }
}
