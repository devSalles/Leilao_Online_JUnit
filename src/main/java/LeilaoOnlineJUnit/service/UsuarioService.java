package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.Enum.StatusLeilao;
import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.dto.usuario.UsuarioRequestDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioResponseDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioUpdateRequestDTO;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.NenhumRegistroException;
import LeilaoOnlineJUnit.infra.exception.UsuarioBloqueadoException;
import LeilaoOnlineJUnit.infra.exception.participante.*;
import LeilaoOnlineJUnit.repository.ItemRepository;
import LeilaoOnlineJUnit.repository.LeilaoRepository;
import LeilaoOnlineJUnit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ItemRepository itemRepository;
    private final LeilaoRepository leilaoRepository;

    @Transactional
    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO usuarioRequestDTO)
    {
        String cpfFormatado = limparCPF(usuarioRequestDTO.cpf());
        if(usuarioRepository.existsByEmail(usuarioRequestDTO.email()))
        {
            throw new EmailRepetidoException();
        }

        if(usuarioRepository.existsByCpf(cpfFormatado))
        {
            throw new CpfRepetidoException();
        }

        Usuario novoUsuario = usuarioRequestDTO.toUsuario();
        novoUsuario.setCpf(cpfFormatado);
        this.usuarioRepository.save(novoUsuario);

        return UsuarioResponseDTO.fromUsuario(novoUsuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long idUser, UsuarioUpdateRequestDTO usuarioUpdtRequestDTO)
    {
        Usuario usuario = buscarIdUsuario(idUser);

        if(usuarioRepository.existsByEmailAndIdNot(usuarioUpdtRequestDTO.email(),idUser))
        {
            throw new EmailRepetidoException();
        }

        usuarioUpdtRequestDTO.UpdateUsuario(usuario);
        this.usuarioRepository.save(usuario);

        return UsuarioResponseDTO.fromUsuario(usuario);
    }

    public UsuarioResponseDTO exibirPorId(Long id)
    {
        Usuario usuario = buscarIdUsuario(id);
        return UsuarioResponseDTO.fromUsuario(usuario);
    }

    public List<UsuarioResponseDTO> exibirTodosUsuarios()
    {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if(usuarios.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro foi encontrado");
        }

        return usuarios.stream().map(UsuarioResponseDTO::fromUsuario).toList();
    }

    public UsuarioResponseDTO exibirPorCpf(String cpf)
    {
        String cpfFormatado = limparCPF(cpf);
        Usuario usuarioCPF = usuarioRepository.findByCpf(cpfFormatado);

        if (usuarioCPF == null)
        {
            throw new CpfNaoEncontradoException();
        }

        return UsuarioResponseDTO.fromUsuario(usuarioCPF);
    }

    public UsuarioResponseDTO exibirPorEmail(String email)
    {
        Usuario usuarioEmail = usuarioRepository.findByEmail(email);
        if (usuarioEmail == null)
        {
            throw new EmailNaoEncontradoException();
        }
        return UsuarioResponseDTO.fromUsuario(usuarioEmail);
    }

    public List<UsuarioResponseDTO> exibirPorStatus(StatusUsuario statusUsuario)
    {
        List<Usuario> usuarioStatus = usuarioRepository.findByStatusUsuario(statusUsuario);
        if (usuarioStatus.isEmpty())
        {
            throw new NenhumRegistroException("Nenhum registro foi encontrado com esse status");
        }
        return usuarioStatus.stream().map(UsuarioResponseDTO::fromUsuario).toList();
    }

    public UsuarioResponseDTO bloquearUsuario(Long idUser)
    {
        Usuario usuarioBloqueado = buscarIdUsuario(idUser);

        if(usuarioBloqueado.getStatusUsuario().equals(StatusUsuario.BLOQUEADO))
        {
            throw new UsuarioBloqueadoException("Usuario já bloqueado");
        }

        usuarioBloqueado.setStatusUsuario(StatusUsuario.BLOQUEADO);
        this.usuarioRepository.save(usuarioBloqueado);
        return  UsuarioResponseDTO.fromUsuario(usuarioBloqueado);
    }

    @Transactional
    public UsuarioResponseDTO removerUsuario(Long idUsuario)
    {
        Usuario usuarioRemover = buscarIdUsuario(idUsuario);

        boolean possuiLeilaoAtivo = itemRepository.existsByProprietarioIdAndLeilaoIdIsNotNull(idUsuario);
        if(possuiLeilaoAtivo)
        {
            throw new PossuiLeilaoAtivoException();
        }

        boolean possuiItemEmLeilao = leilaoRepository.existsByCriadorIdAndStatusLeilaoIn(idUsuario, List.of(StatusLeilao.AGENDADO,StatusLeilao.ABERTO));
        if(possuiItemEmLeilao)
        {
            throw new PossuiItemEmLeilaoException();
        }

        usuarioRepository.delete(usuarioRemover);
        return  UsuarioResponseDTO.fromUsuario(usuarioRemover);
    }

    // --- METODO AUXILIAR ---

    public Usuario buscarIdUsuario(Long idUser)
    {
        return usuarioRepository.findById(idUser).orElseThrow(()->new IdNaoEncontradoException("Usuário não encontrado"));
    }

    private String limparCPF(String cpf)
    {
        return cpf.replaceAll("[^0-9]","");
    }
}
