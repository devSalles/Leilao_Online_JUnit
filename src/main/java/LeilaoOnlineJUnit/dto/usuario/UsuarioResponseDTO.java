package LeilaoOnlineJUnit.dto.usuario;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.entity.Item;
import LeilaoOnlineJUnit.entity.Lance;
import LeilaoOnlineJUnit.entity.Leilao;
import LeilaoOnlineJUnit.entity.Usuario;

import java.util.List;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        StatusUsuario statusUsuario,
        List<Long> idItem,
        List<Long> idLance,
        List<Long> idLeilao
) {

    public static UsuarioResponseDTO fromUsuario(Usuario usuario)
    {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.getStatusUsuario(),
                usuario.getItens().stream().map(Item::getId).toList(),
                usuario.getLances().stream().map(Lance::getId).toList(),
                usuario.getLeilao().stream().map(Leilao::getId).toList());
    }
}
