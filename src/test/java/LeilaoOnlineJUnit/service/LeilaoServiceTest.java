package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.leilao.LeilaoRequestDTO;
import LeilaoOnlineJUnit.dto.leilao.LeilaoResponseDTO;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.ItemFactory;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
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
    public void agendarLeilao()
    {
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();
        Item item = ItemFactory.criarItemPronto(usuario);

        LocalDateTime dataInicio = LocalDateTime.now().plusDays(1);
        LocalDateTime dataFim = LocalDateTime.now().plusDays(2);

        when(usuarioService.buscarIdUsuario(usuario.getId())).thenReturn(usuario);
        when(itemService.buscarID(item.getId())).thenReturn(item);

        LeilaoRequestDTO leilaoRequestDTO = new LeilaoRequestDTO(dataInicio, dataFim, item.getId(), usuario.getId());

        LeilaoResponseDTO response = leilaoService.agendarLeilao(leilaoRequestDTO);

        ArgumentCaptor<Leilao> captor = ArgumentCaptor.forClass(Leilao.class);

        verify(leilaoRepository).save(captor.capture());
        verify(usuarioService).buscarIdUsuario(usuario.getId());
        verify(itemService).buscarID(item.getId());

        Leilao leilao = captor.getValue();

        validarDaddosLeilao(leilao,response);
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
