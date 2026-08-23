package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.usuario.UsuarioRequestDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioResponseDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioUpdateRequestDTO;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.factory.UsuarioFactory;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.NenhumRegistroException;
import LeilaoOnlineJUnit.infra.exception.CpfNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.CpfRepetidoException;
import LeilaoOnlineJUnit.infra.exception.EmailNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.EmailRepetidoException;
import LeilaoOnlineJUnit.repository.ItemRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import LeilaoOnlineJUnit.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        verify(usuarioRepository,times(1)).findById(usuario.getId());

        validarUsuario(usuario,usuarioResponse);
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

    //---GET ALL ---

    @Test
    void pesquisarTodosOsUsarios()
    {
        //Arrange
        Usuario pedro = UsuarioFactory.criarUsuarioPersonalizado(1L,"pedro","60687400058");
        Usuario carlos = UsuarioFactory.criarUsuarioPersonalizado(2L,"carlos","18695885097");

        when(usuarioRepository.findAll()).thenReturn(List.of(pedro,carlos));

        //Act

        List<UsuarioResponseDTO> usuarioResponse = usuarioService.exibirTodosUsuarios();

        //Assert
        assertNotNull(usuarioResponse);
        assertEquals(2,usuarioResponse.size());
        assertEquals("pedro",usuarioResponse.get(0).nome());
        assertEquals("carlos",usuarioResponse.get(1).nome());

        verify(usuarioRepository).findAll();
    }

    @Test
    void lancarExcecaoQuandoListaEstiverVazia()
    {
        //Arrange
        when(usuarioRepository.findAll()).thenReturn(List.of());

        //Act
        assertThrows(NenhumRegistroException.class,()-> usuarioService.exibirTodosUsuarios());

        //Assert
        verify(usuarioRepository).findAll();
    }

    //--- GET CPF ---

    @Test
    void buscarPorCPF()
    {
        //Arrange
        String cpf = "14282943688";
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();

        when(usuarioRepository.findByCpf(cpf)).thenReturn(usuario);

        //Act
        UsuarioResponseDTO usuarioCpfResponse = usuarioService.exibirPorCpf(cpf);

        //Assert
        assertNotNull(usuarioCpfResponse);
        assertEquals("14282943688",usuarioCpfResponse.cpf());

        verify(usuarioRepository).findByCpf(usuario.getCpf());

        validarUsuario(usuario,usuarioCpfResponse);
    }

    @Test
    void deveLancarExcecaoQuandoCpfNaoEncontrado()
    {
        // Arrange
        String cpf = "142.829.436-88";

        when(usuarioRepository.findByCpf("14282943688")).thenReturn(null);

        // Act
        CpfNaoEncontradoException exception = assertThrows(CpfNaoEncontradoException.class, () -> usuarioService.exibirPorCpf(cpf));

        // Assert
        assertEquals("Cpf não encontrado", exception.getMessage());

        verify(usuarioRepository).findByCpf("14282943688");
    }

    //--- GET EMAIL ---

    @Test
    void realizarBuscarPorEmail()
    {
        //Arrange
        String email = "bernardo89@gmail.com";
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();

        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);

        //Act
        UsuarioResponseDTO usuarioResponse = usuarioService.exibirPorEmail(email);

        //Assert
        validarUsuario(usuario,usuarioResponse);

        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void lancarExcecaoQuandoEmailNaoEncontrado()
    {
        //Arrange
        String email = "bernardo@gmail.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        //Act
        EmailNaoEncontradoException exception = assertThrows(EmailNaoEncontradoException.class,()->usuarioService.exibirPorEmail(email));

        //Assert
        assertEquals("Email não encontrado",exception.getMessage());

        verify(usuarioRepository).findByEmail(email);
    }

    //--- GET EMAIL ---

    @Test
    void buscarPorStatus()
    {
        //Arrange
        StatusUsuario statusUsuario = StatusUsuario.ATIVO;
        Usuario usuario = UsuarioFactory.criarUsuarioPronto();

        when(usuarioRepository.findByStatusUsuario(statusUsuario)).thenReturn(List.of(usuario));

        //Act
        List<UsuarioResponseDTO> usuarioResponseDTO = usuarioService.exibirPorStatus(statusUsuario);

        //Assert
        assertNotNull(usuarioResponseDTO);
        assertEquals(1,usuarioResponseDTO.size());

        assertEquals(usuario.getId(),usuarioResponseDTO.getFirst().id());
        assertEquals(usuario.getNome(),usuarioResponseDTO.getFirst().nome());
        assertEquals(usuario.getEmail(),usuarioResponseDTO.getFirst().email());
        assertEquals(usuario.getCpf(),usuarioResponseDTO.getFirst().cpf());
        assertEquals(usuario.getStatusUsuario(),usuarioResponseDTO.getFirst().statusUsuario());

        verify(usuarioRepository).findByStatusUsuario(statusUsuario);
    }

//    @Test
//    void lancarExcecaoQuandoStatusNaoEncontrado()
//    {
//        StatusUsuario statusUsuario = StatusUsuario.BLOQUEADO;
//
//        when(usuarioRepository.findByStatusUsuario(statusUsuario)).thenReturn(null);
//
//        StatusInc
//    }

    // --- METODO AUXILIAR ---

    public void validarUsuario(Usuario usuario, UsuarioResponseDTO usuarioResponseDTO)
    {
        assertAll(()-> assertNotNull(usuarioResponseDTO),
                ()-> assertEquals(usuario.getId(),usuarioResponseDTO.id()),
                ()-> assertEquals(usuario.getNome(),usuarioResponseDTO.nome()),
                ()-> assertEquals(usuario.getEmail(),usuarioResponseDTO.email())
        );
    }
}
