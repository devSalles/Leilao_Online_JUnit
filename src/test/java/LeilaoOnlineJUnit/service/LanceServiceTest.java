package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.dto.lance.LanceResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.ItemFactory;
import LeilaoOnlineJUnit.factory.LanceFactory;
import LeilaoOnlineJUnit.factory.LeilaoFactory;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.repository.LanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LanceServiceTest {

    @Mock
    UsuarioService usuarioService;

    @Mock
    LanceRepository lanceRepository;

    @Mock
    LanceRepository leilaoRepository;

    @InjectMocks
    LanceService lanceService;


    private Usuario usuario;
    private Leilao leilao;
    private Lance lance;

    @BeforeEach
    void setUp()
    {
        usuario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(usuario);
        leilao = LeilaoFactory.criarLeilaoPronto(item, usuario);
        lance = LanceFactory.criarLancePronto(usuario, leilao);
    }

    @Test
    void deveBuscarLancePorIdComSucesso()
    {
        //Arrange
        when(lanceRepository.findById(1L)).thenReturn(Optional.of(lance));

        //Act
        LanceResponseDTO resultado = lanceService.buscarLance(1L);

        //Assert
        assertEquals(LanceResponseDTO.fromLance(lance), resultado);
        verify(lanceRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoLanceNaoEncontradoPorId()
    {
        when(lanceRepository.findById(99L)).thenReturn(Optional.empty());

        IdNaoEncontradoException excecao = assertThrows(IdNaoEncontradoException.class, () -> lanceService.buscarLance(99L));

        assertEquals("ID de lance não encontrado", excecao.getMessage());
        verify(lanceRepository).findById(99L);
    }
}
