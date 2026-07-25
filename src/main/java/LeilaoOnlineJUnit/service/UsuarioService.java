package LeilaoOnlineJUnit.service;

import LeilaoOnlineJUnit.dto.usuario.UsuarioRequestDTO;
import LeilaoOnlineJUnit.dto.usuario.UsuarioResponseDTO;
import LeilaoOnlineJUnit.entity.Usuario;
import LeilaoOnlineJUnit.infra.exception.CpfRepetidoException;
import LeilaoOnlineJUnit.infra.exception.EmailRepetidoException;
import LeilaoOnlineJUnit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO usuarioRequestDTO)
    {
        if(usuarioRepository.existsByEmail(usuarioRequestDTO.email()))
        {
            throw new EmailRepetidoException();
        }

        if(usuarioRepository.existsByCpf(usuarioRequestDTO.cpf()))
        {
            throw new CpfRepetidoException();
        }

        Usuario novoUsuario = usuarioRequestDTO.toUsuario();
        this.usuarioRepository.save(novoUsuario);

        return UsuarioResponseDTO.fromUsuario(novoUsuario);
    }
}
