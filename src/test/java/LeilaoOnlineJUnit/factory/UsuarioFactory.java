package LeilaoOnlineJUnit.factory;

import LeilaoOnlineJUnit.Enum.StatusUsuario;
import LeilaoOnlineJUnit.entity.Usuario;
import io.swagger.v3.oas.models.media.UUIDSchema;

public class UsuarioFactory {

    public Usuario criarUsuarioPronto(){

        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNome("Bernardo");
        usuario.setCpf("14282943688");
        usuario.setEmail("bernardo89@gmail.com");
        usuario.setStatusUsuario(StatusUsuario.ATIVO);

        return usuario;
    }

    public Usuario criarUsuarioPersonalizado(Long id, String nome, String cpf)
    {
        Usuario usuario = new Usuario();

        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(nome.toLowerCase().replace(" ","") + "@gmail.com");

        return usuario;
    }
}
