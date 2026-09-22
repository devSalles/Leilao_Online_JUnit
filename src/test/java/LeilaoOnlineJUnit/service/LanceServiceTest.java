package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.lance.LanceRequestDTO;
import LeilaoOnlineJUnit.dto.lance.LanceResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.ItemFactory;
import LeilaoOnlineJUnit.factory.LanceFactory;
import LeilaoOnlineJUnit.factory.LeilaoFactory;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.*;
import LeilaoOnlineJUnit.repository.LanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LanceServiceTest {

    @Mock
    UsuarioService usuarioService;

    @Mock
    LeilaoService leilaoService;

    @Mock
    LanceRepository lanceRepository;

    @InjectMocks
    LanceService lanceService;

    private Usuario usuario;
    private Usuario participante;
    private Leilao leilao;
    private Lance lance;

    @BeforeEach
    void setUp() {
        usuario = UsuarioFactory.criarUsuarioPronto();

        Item item = ItemFactory.criarItemPronto(usuario);

        leilao = LeilaoFactory.criarLeilaoPronto(item, usuario);

        lance = LanceFactory.criarLancePronto(usuario, leilao);

        participante = UsuarioFactory.criarUsuarioPronto();
        participante.setId(2L);
    }

    @Test
    void deveRealizarLanceComSucesso() {

        leilao.getItem().setValorInicial(new BigDecimal("10000.00"));

        LanceRequestDTO request = new LanceRequestDTO(new BigDecimal("15000.00"), participante.getId(), leilao.getId());

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        when(lanceRepository.findFirstByLeilaoOrderByValorDesc(leilao)).thenReturn(Optional.empty());

        LanceResponseDTO response = lanceService.realizarLance(request);

        assertNotNull(response);
        verify(lanceRepository).save(any(Lance.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioForProprietario() {

        leilao.setCriador(usuario);

        when(usuarioService.buscarIdUsuario(usuario.getId())).thenReturn(usuario);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        LanceRequestDTO request = new LanceRequestDTO(new BigDecimal("15000.00"), usuario.getId(), leilao.getId());

        assertThrows(UsuarioProprietarioException.class, () -> lanceService.realizarLance(request));

        verify(lanceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioEstiverBloqueado() {

        participante.setStatusUsuario(StatusUsuario.BLOQUEADO);

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        LanceRequestDTO request = new LanceRequestDTO(new BigDecimal("15000.00"), participante.getId(), leilao.getId());

        assertThrows(UsuarioBloqueadoException.class, () -> lanceService.realizarLance(request));

        verify(lanceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoLeilaoNaoEstiverAberto() {

        leilao.setStatusLeilao(StatusLeilao.AGENDADO);

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        LanceRequestDTO request = new LanceRequestDTO(new BigDecimal("15000.00"), participante.getId(), leilao.getId());

        assertThrows(LeilaoNaoAbertoException.class, () -> lanceService.realizarLance(request));

        verify(lanceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoValorDoLanceForInvalido() {

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        assertAll(
                () -> assertThrows(ValorLanceInvalidoException.class, () -> lanceService.realizarLance(new LanceRequestDTO(null, participante.getId(), leilao.getId()))),
                () -> assertThrows(ValorLanceInvalidoException.class, () -> lanceService.realizarLance(new LanceRequestDTO(BigDecimal.ZERO, participante.getId(), leilao.getId()))),
                () -> assertThrows(ValorLanceInvalidoException.class, () -> lanceService.realizarLance(new LanceRequestDTO(new BigDecimal("-10"), participante.getId(), leilao.getId())))
        );

        verify(lanceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoPrimeiroLanceForMenorQueValorInicial() {

        leilao.getItem().setValorInicial(new BigDecimal("20000.00"));

        LanceRequestDTO request = new LanceRequestDTO(new BigDecimal("15000.00"), participante.getId(), leilao.getId());

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        when(lanceRepository.findFirstByLeilaoOrderByValorDesc(leilao)).thenReturn(Optional.empty());

        assertThrows(PrimeiroLanceInvaidoException.class, () -> lanceService.realizarLance(request));

        verify(lanceRepository, never()).save(any());
    }

    @Test
    void deveAceitarPrimeiroLanceIgualAoValorInicial() {

        leilao.getItem().setValorInicial(new BigDecimal("15000.00"));

        LanceRequestDTO request = new LanceRequestDTO(new BigDecimal("15000.00"), participante.getId(), leilao.getId());

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        when(lanceRepository.findFirstByLeilaoOrderByValorDesc(leilao)).thenReturn(Optional.empty());

        LanceResponseDTO response = lanceService.realizarLance(request);

        assertNotNull(response);
        verify(lanceRepository).save(any(Lance.class));
    }

    @Test
    void deveLancarExcecaoQuandoLanceForMenorOuIgualAoMaiorLance() {

        Lance maiorLance = LanceFactory.criarLancePersonalizado(2L, new BigDecimal("20000.00"), lance.getDataHora(), leilao, usuario);

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        when(lanceRepository.findFirstByLeilaoOrderByValorDesc(leilao)).thenReturn(Optional.of(maiorLance));

        assertAll(() -> assertThrows(LanceInvalidoException.class, () -> lanceService.realizarLance(new LanceRequestDTO(new BigDecimal("15000.00"), participante.getId(), leilao.getId()))),
                () -> assertThrows(LanceInvalidoException.class, () -> lanceService.realizarLance(new LanceRequestDTO(new BigDecimal("20000.00"), participante.getId(), leilao.getId())))
        );

        verify(lanceRepository, never()).save(any());
    }

    @Test
    void deveAceitarLanceMaiorQueMaiorLance() {

        Lance maiorLance = LanceFactory.criarLancePersonalizado(2L, new BigDecimal("20000.00"), lance.getDataHora(), leilao, usuario);

        when(lanceRepository.findFirstByLeilaoOrderByValorDesc(leilao)).thenReturn(Optional.of(maiorLance));

        when(usuarioService.buscarIdUsuario(participante.getId())).thenReturn(participante);

        when(leilaoService.buscarLeilaoID(leilao.getId())).thenReturn(leilao);

        LanceRequestDTO request = new LanceRequestDTO(new BigDecimal("25000.00"), participante.getId(), leilao.getId());

        LanceResponseDTO response = lanceService.realizarLance(request);

        assertNotNull(response);
        verify(lanceRepository).save(any(Lance.class));
    }

    // GET BY ID

    @Test
    void deveBuscarLancePorIdComSucesso() {

        when(lanceRepository.findById(1L)).thenReturn(Optional.of(lance));

        LanceResponseDTO resultado = lanceService.buscarLance(1L);

        assertEquals(LanceResponseDTO.fromLance(lance), resultado);

        verify(lanceRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoLanceNaoEncontradoPorId() {

        when(lanceRepository.findById(99L)).thenReturn(Optional.empty());

        IdNaoEncontradoException excecao = assertThrows(IdNaoEncontradoException.class, () -> lanceService.buscarLance(99L));

        assertEquals("ID de lance não encontrado", excecao.getMessage());

        verify(lanceRepository).findById(99L);
    }

    // GET BY ID LANCE POR LEILAO-ID

    @Test
    void deveBuscarLancesPorLeilaoComSucesso() {

        Lance outroLance = LanceFactory.criarLancePronto(usuario, leilao);

        when(lanceRepository.findByLeilaoId(leilao.getId())).thenReturn(List.of(lance, outroLance));

        List<LanceResponseDTO> resultado = lanceService.buscarLancesPorLeilao(leilao.getId());

        assertEquals(2, resultado.size());

        assertEquals(LanceResponseDTO.fromLance(lance), resultado.get(0));

        assertEquals(LanceResponseDTO.fromLance(outroLance), resultado.get(1));

        verify(lanceRepository).findByLeilaoId(leilao.getId());
    }

    @Test
    void deveLancarExcecaoQuandoLeilaoNaoPossuiLances() {

        when(lanceRepository.findByLeilaoId(1L)).thenReturn(Collections.emptyList());

        NenhumRegistroException excecao = assertThrows(NenhumRegistroException.class, () -> lanceService.buscarLancesPorLeilao(1L));

        assertEquals("Nenhum registro foi encontrado", excecao.getMessage());

        verify(lanceRepository).findByLeilaoId(1L);
    }

    // GET ALL

    @Test
    void deveListarTodosOsLancesComSucesso() {

        Lance outroLance = LanceFactory.criarLancePronto(usuario, leilao);

        when(lanceRepository.findAll()).thenReturn(List.of(lance, outroLance));

        List<LanceResponseDTO> resultado = lanceService.listarLances();

        assertEquals(2, resultado.size());

        assertEquals(LanceResponseDTO.fromLance(lance), resultado.get(0));

        assertEquals(LanceResponseDTO.fromLance(outroLance), resultado.get(1));

        verify(lanceRepository).findAll();
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistemLances() {

        when(lanceRepository.findAll()).thenReturn(List.of());

        NenhumRegistroException excecao = assertThrows(NenhumRegistroException.class, () -> lanceService.listarLances());

        assertEquals("Nenhum registro foi encontrado", excecao.getMessage());

        verify(lanceRepository).findAll();
    }
}