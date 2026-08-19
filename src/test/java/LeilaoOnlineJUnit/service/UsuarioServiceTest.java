package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.dto.usuario.UsuarioRequestDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioResponseDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioUpdateRequestDTO;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        //Arrange
        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Bernardo","bernardo@gmail.com","142.829.436-88");

        //Act
        UsuarioResponseDTO usuarioResponse = usuarioService.salvarUsuario(usuarioRequestDTO);

        //Assert
        assertEquals("Bernardo",usuarioResponse.nome());
        assertEquals("bernardo@gmail.com",usuarioResponse.email());
        assertEquals("14282943688",usuarioResponse.cpf());

        usuarioRepository.save(any(Usuario.class));
    }

    @Test
    void lancarExcecaoQuandoEmailExistente()
    {
        //Arrange
        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Bernardo","bernardo@gmail.com","142.829.436-88");

        when(usuarioRepository.existsByEmail(usuarioRequestDTO.email())).thenReturn(true);

        //Act
        assertThrows(EmailRepetidoException.class,()->usuarioService.salvarUsuario(usuarioRequestDTO));

        //Assert
        verify(usuarioRepository,never()).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfExistente()
    {
        //Arrange
        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Bernardo","bernardo@gmail.com","142.829.436-88");

        //Act
        when(usuarioRepository.existsByCpf("14282943688")).thenReturn(true);

        //Assert
        assertThrows(CpfRepetidoException.class,()->usuarioService.salvarUsuario(usuarioRequestDTO));

        verify(usuarioRepository,never()).save(any(Usuario.class));
    }

    //--- PUT Usuario ---

    @Test
    void atualizarUsuario()
    {
        //Arrange
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();

        UsuarioUpdateRequestDTO usuarioUpdtRequestDTO = new UsuarioUpdateRequestDTO("Bernardo","bernardo@gmail.com");

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndIdNot(usuarioUpdtRequestDTO.email(), usuario.getId())).thenReturn(false);

        //Act
        UsuarioResponseDTO usuarioResponse = usuarioService.atualizarUsuario(usuario.getId(),usuarioUpdtRequestDTO);

        //Assert
        assertEquals("Bernardo",usuarioResponse.nome());
        assertEquals("bernardo@gmail.com",usuarioResponse.email());

        verify(usuarioRepository,times(1)).findById(usuario.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void lancarExcecaoQuandoEmailRepetido()
    {
        //Arrange
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();
        UsuarioUpdateRequestDTO usuarioUpdateRequestDTO = new UsuarioUpdateRequestDTO("Bernardo","bernardo@gmail.com");

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndIdNot(usuarioUpdateRequestDTO.email(), usuario.getId())).thenReturn(true);

        //Act
        EmailRepetidoException emailRepetidoException = assertThrows(EmailRepetidoException.class,()-> usuarioService.atualizarUsuario(usuario.getId(), usuarioUpdateRequestDTO));

        //Assert
        assertEquals("Email já cadastrado",emailRepetidoException.getMessage());

        verify(usuarioRepository,never()).save(any(Usuario.class));
        verify(usuarioRepository).findById(usuario.getId());
    }

    // --- GET ID ---

    @Test
    void buscarUsuarioPorId()
    {
        //Arrange
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        //Act
        UsuarioResponseDTO usuarioResponse = usuarioService.exibirPorId(usuario.getId());

        //Assert
        assertEquals("Bernardo",usuarioResponse.nome());
        assertEquals("14282943688",usuarioResponse.cpf());
        assertEquals("bernardo89@gmail.com",usuarioResponse.email());

        verify(usuarioRepository,times(1)).findById(usuario.getId());
    }

    @Test
    void deveLancarExcecaoQuandoIdUsuarioNaoEncontrado()
    {
        //Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        //Act
        IdNaoEncontradoException idNaoEncontradoException = assertThrows(IdNaoEncontradoException.class,()-> usuarioService.exibirPorId(1L));
        assertEquals("Usuário não encontrado",idNaoEncontradoException.getMessage());

        //Assert
        verify(usuarioRepository,times(1)).findById(1L);
    }
}
