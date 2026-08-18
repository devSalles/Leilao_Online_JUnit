package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.dto.usuario.UsuarioRequestDTO;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.participante.CpfRepetidoException;
import LeilaoOnlineJUnit.infra.exception.participante.EmailRepetidoException;
import LeilaoOnlineJUnit.repository.ItemRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import LeilaoOnlineJUnit.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {


    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    LeilaoRepository leilaoRepository;

    @InjectMocks
    UsuarioService usuarioService;

    // --- POST Usuario ---

    @Test
    void deveSalvarUsuario()
    {
        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Bernardo","bernardo@gmail.com","142.829.436-88");

        usuarioService.salvarUsuario(usuarioRequestDTO);

        usuarioRepository.save(any(Usuario.class));
    }

    @Test
    void lancarExcecaoQuandoEmailExistente()
    {
        //Arrange
        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Bernardo","bernardo@gmail.com","142.829.436-88");

        when(usuarioRepository.existsByEmail("bernardo@gmail.com")).thenReturn(true);

        //Act
        assertThrows(EmailRepetidoException.class,()->usuarioService.salvarUsuario(usuarioRequestDTO));

        //Assert
        verify(usuarioRepository,never()).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfExistente()
    {
        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Bernardo","bernardo@gmail.com","142.829.436-88");

        when(usuarioRepository.existsByCpf("14282943688")).thenReturn(true);

        assertThrows(CpfRepetidoException.class,()->usuarioService.salvarUsuario(usuarioRequestDTO));

        verify(usuarioRepository,never()).save(any(Usuario.class));
    }
}
